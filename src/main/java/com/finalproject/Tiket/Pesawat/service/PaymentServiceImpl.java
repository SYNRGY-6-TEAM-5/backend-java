package com.finalproject.Tiket.Pesawat.service;

import com.finalproject.Tiket.Pesawat.dto.PaymentResponseDTO;
import com.finalproject.Tiket.Pesawat.dto.payment.request.CreateVaPaymentRequest;
import com.finalproject.Tiket.Pesawat.dto.payment.request.RequestWebhookXendit;
import com.finalproject.Tiket.Pesawat.exception.ExceptionHandling;
import com.finalproject.Tiket.Pesawat.exception.UnauthorizedHandling;
import com.finalproject.Tiket.Pesawat.model.Booking;
import com.finalproject.Tiket.Pesawat.model.User;
import com.finalproject.Tiket.Pesawat.repository.BookingRepository;
import com.finalproject.Tiket.Pesawat.repository.UserRepository;
import com.finalproject.Tiket.Pesawat.security.service.UserDetailsImpl;
import com.finalproject.Tiket.Pesawat.utils.Utils;
import com.xendit.Xendit;
import com.xendit.enums.BankCode;
import com.xendit.exception.XenditException;
import com.xendit.model.FixedVirtualAccount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

import static com.finalproject.Tiket.Pesawat.utils.Constants.PAYMENT_EXPIRED_TIME;
import static org.bouncycastle.asn1.ua.DSTU4145NamedCurves.params;


@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${aeroswift.xendit.secretkey}")
    private String xenditSecretkey;

    @Value("${aeroswift.xendit.callback-token}")
    private String xenditCallbackToken;

    @Override
    public PaymentResponseDTO createPaymentXendit(CreateVaPaymentRequest request) {
        try {
            Xendit.apiKey = xenditSecretkey;
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetailsImpl) {
                Optional<User> userOptional = userRepository
                        .findByEmailAddress(((UserDetailsImpl) principal).getUsername());
                if (userOptional.isEmpty()) {
                    throw new UnauthorizedHandling("User Not Found");
                }

                if (userOptional.get().getFullname() == null){
                    throw new UnauthorizedHandling("User has not set their full name in the profile");
                }

                // find booking
                Optional<Booking> bookingOptional = bookingRepository.findById(request.getBookingId());
                if (bookingOptional.isEmpty()) {
                    throw new ExceptionHandling("Booking Not Found");
                }

                Booking booking = bookingOptional.get();
                User user = userOptional.get();
                // todo check apakah memang booking id mempunyai relasi dengan user
//                if (!booking.getUser().getUuid().equals(user.getUuid())){
//                    throw new ExceptionHandling("User does not have a booking");
//                }
                String externalId = "external_id_" + Instant.now().getEpochSecond() + "_" + user.getUuid();

                TimeZone gmtMinus7TimeZone = TimeZone.getTimeZone("GMT+0");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                dateFormat.setTimeZone(gmtMinus7TimeZone);
                Date expiredPayment = new Date(System.currentTimeMillis() + PAYMENT_EXPIRED_TIME);
                String formattedExpirationDateGMTMinus7 = dateFormat.format(expiredPayment);
                log.info(formattedExpirationDateGMTMinus7 + "GMT +7" + expiredPayment);

                Map<String, Object> params = new HashMap<>();
                params.put("external_id", externalId);
                params.put("bank_code", request.getBankCode());
                params.put("name", user.getFullname());
                params.put("expected_amount", booking.getTotalAmount());
                params.put("is_single_use", true);
                params.put("expiration_date", formattedExpirationDateGMTMinus7);

                FixedVirtualAccount virtualAccount;
                try {
                    virtualAccount = FixedVirtualAccount.createClosed(params);
                } catch (XenditException e) {
                    log.error(e.getMessage());
                    throw new ExceptionHandling(e.getMessage());
                }

                booking.setExpiredTime(expiredPayment);
                booking.setExternalId(externalId);
                booking.setUpdatedAt(Utils.getCurrentDateTimeAsDate());
                bookingRepository.save(booking);

                return PaymentResponseDTO.builder()
                        .success(true)
                        .va(virtualAccount.getAccountNumber())
                        .externalId(externalId)
                        .expiredPayment(expiredPayment)
                        .build();

            } else if (principal instanceof String) {
                throw new UnauthorizedHandling("User not authenticated");
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ExceptionHandling(e.getMessage());
        }
        throw new ExceptionHandling("Internal Server Error");
    }

    @Override
    public String createVirtualAccountXenditWebhook(String xCallbackToken, RequestWebhookXendit requestWebhook) {
        Xendit.apiKey = xenditSecretkey;

        if (!xCallbackToken.equals(xenditCallbackToken)) {
            throw new UnauthorizedHandling("Failed Signature");
        }

        log.info("saving va to booking table");

        return "success";
    }

    @Override
    public String paidXenditVirtualAccountWebhook(String xCallbackToken, RequestWebhookXendit requestWebhook) {
        Xendit.apiKey = xenditSecretkey;

        if (!xCallbackToken.equals(xenditCallbackToken)) {
            throw new UnauthorizedHandling("Failed Signature");
        }

        // save update status paid in booking > ticket issued
        log.info("saving va to booking table");

        return "paid";
    }

}
