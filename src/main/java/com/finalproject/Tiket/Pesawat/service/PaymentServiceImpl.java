package com.finalproject.Tiket.Pesawat.service;

import com.finalproject.Tiket.Pesawat.dto.PaymentResponseDTO;
import com.finalproject.Tiket.Pesawat.dto.firebase.request.NotificationRequest;
import com.finalproject.Tiket.Pesawat.dto.payment.request.CreateVaPaymentRequest;
import com.finalproject.Tiket.Pesawat.dto.payment.request.RequestWebhookXendit;
import com.finalproject.Tiket.Pesawat.dto.payment.request.RequestWebhookXenditPaid;
import com.finalproject.Tiket.Pesawat.dto.payment.response.SupportPaymentResponse;
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
import com.xendit.model.FixedPaymentCode;
import com.xendit.model.FixedVirtualAccount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.finalproject.Tiket.Pesawat.utils.Constants.CONSTANT_PAYMENT_STATUS_SUCCESS;


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

    @Value("${aeroswift.xendit.baseUrl}")
    private String xenditApiBaseUrl;

    @Value("${aeroswift.xendit.username}")
    private String username;

    @Autowired
    private FCMService fcmService;

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

                if (userOptional.get().getFullname() == null) {
                    throw new UnauthorizedHandling("User has not set their full name in the profile");
                }

                Optional<Booking> bookingOptional = bookingRepository.findById(request.getBookingId());

                if (bookingOptional.isEmpty()) {
                    throw new ExceptionHandling("Booking Not Found");
                }

                if (bookingOptional.get().getBookingCode() != null) {
                    throw new ExceptionHandling("Booking code not generated. This booking with the specified ID has already been generated previously.");
                }

                Booking booking = bookingOptional.get();
                User user = userOptional.get();
                if (!booking.getUser().getUuid().equals(user.getUuid())) {
                    throw new ExceptionHandling("User does not have a booking");
                }
                String externalId = "external_id_" + Instant.now().getEpochSecond() + "_" + user.getUuid();

                TimeZone gmtMinus7TimeZone = TimeZone.getTimeZone("GMT+0");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                dateFormat.setTimeZone(gmtMinus7TimeZone);
                String formattedExpirationDateGMTMinus7 = dateFormat.format(booking.getExpiredTime());

                Map<String, Object> params = new HashMap<>();
                params.put("external_id", externalId);
                params.put("bank_code", request.getBankCode());
                params.put("name", user.getFullname());
                params.put("expected_amount", booking.getTotalAmount());
                params.put("is_single_use", true);
                params.put("expiration_date", formattedExpirationDateGMTMinus7);

                FixedVirtualAccount virtualAccount;
                try {
                    log.info("create va " + user.getUuid().toString());
                    virtualAccount = FixedVirtualAccount.createClosed(params);
                } catch (XenditException e) {
                    log.error(e.getMessage());
                    throw new ExceptionHandling(e.getMessage());
                }

                booking.setExternalId(externalId);
                bookingRepository.save(booking);

                return PaymentResponseDTO.builder()
                        .success(true)
                        .va(virtualAccount.getAccountNumber())
                        .externalId(externalId)
                        .expiredPayment(booking.getExpiredTime())
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

        Optional<Booking> bookingOptional = bookingRepository.findByExternalId(requestWebhook.getExternal_id());

        if (bookingOptional.isPresent()) {
            Booking booking = bookingOptional.get();
            booking.setPaymentMethod(requestWebhook.getBank_code());
            booking.setBookingCode(Utils.generateBookingCode(booking.getBookingId(), booking.getUser().getUuid().toString()));
            booking.setUpdatedAt(Utils.getCurrentDateTimeAsDate());
            bookingRepository.save(booking);
            NotificationRequest notificationRequest = NotificationRequest.builder()
                    .title("Virtual Account Created Successfully") // todo create constant
                    .body("Your virtual account has been successfully created for payment with "
                            + requestWebhook.getBank_code() + ".")
                    .token(booking.getUser().getFcmToken())
                    .build();
            if (booking.getUser().getFcmToken() != null) {
                try {
                    fcmService.sendMessageToToken(notificationRequest);
                } catch (InterruptedException | ExecutionException e) {
                    log.error(e.getMessage());
                }
            }
            log.info("success create va");
            return "success";
        } else {
            log.error("failed ");
            return "failed booking not found for external_id";
        }
    }


    @Override
    public String paidXenditVirtualAccountWebhook(String xCallbackToken, RequestWebhookXenditPaid requestWebhook) {
        Xendit.apiKey = xenditSecretkey;

        if (!xCallbackToken.equals(xenditCallbackToken)) {
            throw new UnauthorizedHandling("Failed Signature");
        }

        // todo issued ticket
        Optional<Booking> bookingOptional = bookingRepository.findByExternalId(requestWebhook.getExternal_id());

        if (bookingOptional.isPresent()) {
            Booking booking = bookingOptional.get();
            booking.setStatus(CONSTANT_PAYMENT_STATUS_SUCCESS);
            booking.setPaymentId(requestWebhook.getPayment_id());

            // Convert transaction_timestamp to GMT+7
            Instant instant = requestWebhook.getTransaction_timestamp().toInstant();
            ZoneId zoneId = ZoneId.of("GMT+7");
            ZonedDateTime gmtPlus7 = ZonedDateTime.ofInstant(instant, zoneId);
            booking.setUpdatedAt(Date.from(gmtPlus7.toInstant()));

            bookingRepository.save(booking);
            NotificationRequest paymentSuccessNotification = NotificationRequest.builder()
                    .title("Payment Successful") // todo create constant
                    .body("Your payment for booking " + booking.getBookingCode() + " has been successfully paid.")
                    .token(booking.getUser().getFcmToken())
                    .build();

            if (booking.getUser().getFcmToken() != null) {
                try {
                    fcmService.sendMessageToToken(paymentSuccessNotification);
                } catch (InterruptedException | ExecutionException e) {
                    log.error(e.getMessage());
                }
            }

            log.info("saving va to booking table");
            return "success";
        } else {
            log.error("failed ");
            return "failed booking not found for external_id";
        }
    }

    @Override
    public SupportPaymentResponse getSupportPayment() {
        List<String> bankCodes = Arrays.stream(BankCode.values())
                .map(BankCode::getText)
                .collect(Collectors.toList());

        List<String> retailOutletNames = Arrays.stream(FixedPaymentCode.RetailOutletName.values())
                .map(Enum::name)
                .collect(Collectors.toList());

        return SupportPaymentResponse.builder()
                .banks(bankCodes)
                .retails(retailOutletNames)
                .build();
    }

    @Override
    public ResponseEntity<Object> simulatePayment(String amount, String externalId) {
        String requestBody = "{\"amount\":" + amount + "}";
        String apiUrl = xenditApiBaseUrl + "external_id=" + externalId + "/simulate_payment";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(username, "");
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    apiUrl, HttpMethod.POST, requestEntity, String.class);

            return ResponseEntity.ok(responseEntity.getBody());
        } catch (HttpClientErrorException.BadRequest e) {
            String errorMessage = extractErrorMessage(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred");
        }
    }

    private String extractErrorMessage(HttpClientErrorException.BadRequest e) {
        try {
            return e.getResponseBodyAsString();
        } catch (Exception ex) {
            return "Bad Request: " + e.getMessage();
        }
    }

}
