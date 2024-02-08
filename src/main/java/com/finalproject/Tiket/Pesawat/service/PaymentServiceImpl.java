package com.finalproject.Tiket.Pesawat.service;

import com.finalproject.Tiket.Pesawat.dto.PaymentResponseDTO;
import com.finalproject.Tiket.Pesawat.dto.StripeDTO;
import com.finalproject.Tiket.Pesawat.dto.payment.request.RequestWebhookXendit;
import com.finalproject.Tiket.Pesawat.exception.ExceptionHandling;
import com.finalproject.Tiket.Pesawat.exception.UnauthorizedHandling;
import com.finalproject.Tiket.Pesawat.model.Booking;
import com.finalproject.Tiket.Pesawat.model.User;
import com.finalproject.Tiket.Pesawat.repository.BookingRepository;
import com.finalproject.Tiket.Pesawat.repository.UserRepository;
import com.finalproject.Tiket.Pesawat.security.service.UserDetailsImpl;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import com.xendit.Xendit;
import com.xendit.enums.BankCode;
import com.xendit.exception.XenditException;
import com.xendit.model.FixedVirtualAccount;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.finalproject.Tiket.Pesawat.utils.Constants.*;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${aeroswift.stripe.apikey}")
    private String stripeApiKey;

    @Value("${aeroswift.stripe.endpointSecret}")
    private String stripeEndpointSecret;

    @Value("${aeroswift.xendit.secretkey}")
    private String xenditSecretkey;


    @Override
    public PaymentResponseDTO createPaymentSession(StripeDTO stripeDto) throws StripeException {
        Stripe.apiKey = stripeApiKey;
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(CONSTANT_PAYMENT_SUCCESS_URL)
                .setCancelUrl(CONSTANT_PAYMENT_FAILED_URL)
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency(CONSTANT_CURRENCY)
                                                .setUnitAmount((long) (stripeDto.getAmount() * 100))
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Total Amount")
                                                                .build())
                                                .build())
                                .build())
                .build();

        return PaymentResponseDTO.builder()
                .success(true)
                .expiredPayment(null) // todo handle expired payment
                .urlPayment(Session.create(params).getUrl())
                .build();
    }

    @Override
    public Object webhook(HttpServletRequest request, HttpServletResponse response, String payload) {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetailsImpl) {
                UserDetailsImpl userDetails = (UserDetailsImpl) principal;

                Optional<User> userOptional = userRepository.findByEmailAddress(userDetails.getUsername());

                if (userOptional.isEmpty()) {
                    throw new ExceptionHandling("User Not Found");
                }

                String endpointSecret = stripeEndpointSecret;
                Stripe.apiKey = stripeApiKey;
                String sigHeader = request.getHeader(STRIPE_HEADER_REQEUST);

                Event event; // init

                try {
                    event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
                } catch (SignatureVerificationException e) {
                    response.setStatus(400);
                    return "Failed Signature";
                }

                if (STRIPE_SESSION_COPMLETED.equals(event.getType())) {
                    // success
                    EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
                    if (dataObjectDeserializer.getObject().isPresent()) {
                        Session session = (Session) dataObjectDeserializer.getObject().get();

                        Booking booking = Booking.builder()
                                .user(userOptional.get())
                                .status(CONSTANT_PAYMENT_STATUS_SUCCESS)
                                .build();
                        bookingRepository.save(booking);
                        log.info("Payment succeeded : " +
                                session.getCustomerDetails().getName() + " : " + session.getAmountTotal() / 100);
                    }

                } else {
                    // failed
                    Booking booking = Booking.builder()
                            .user(userOptional.get())
                            .status(CONSTANT_PAYMENT_STATUS_FAILED)
                            .build();
                    bookingRepository.save(booking);
                    response.setStatus(400);
                    return "";
                }

            } else if (principal instanceof String) {
                throw new UnauthorizedHandling("User not authenticated");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ExceptionHandling(e.getMessage());
        }
        response.setStatus(200);
        return "";
    }

    @Override
    public String createPaymentXendit() {
        Xendit.apiKey = xenditSecretkey;
        Map<String, Object> params = new HashMap<>();
        params.put("external_id", "my_external_id" + new Date());
        params.put("bank_code", BankCode.BNI.getText());
        params.put("name", "John Doe");

        FixedVirtualAccount virtualAccount = null;
        try {
            virtualAccount = FixedVirtualAccount.createOpen(params);
        } catch (XenditException e) {
            log.error(e.getMessage());
        }


        log.info(virtualAccount.getAccountNumber());
        virtualAccount.setStatus("PENDING");

        return virtualAccount.getId();
    }

    @Override
    public String createVirtualAccountXenditWebhook(RequestWebhookXendit requestWebhook) {
        return null;
    }

}
