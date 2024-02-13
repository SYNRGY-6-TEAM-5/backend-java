package com.finalproject.Tiket.Pesawat.controller;

import com.finalproject.Tiket.Pesawat.dto.PaymentResponseDTO;
import com.finalproject.Tiket.Pesawat.dto.payment.request.CreateVaPaymentRequest;
import com.finalproject.Tiket.Pesawat.dto.payment.request.RequestWebhookXendit;
import com.finalproject.Tiket.Pesawat.dto.payment.request.RequestWebhookXenditPaid;
import com.finalproject.Tiket.Pesawat.dto.payment.response.SupportPaymentResponse;
import com.finalproject.Tiket.Pesawat.service.PaymentService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/xendit-payment/create-va")
    public ResponseEntity<PaymentResponseDTO> paymentXendit(@Valid @RequestBody CreateVaPaymentRequest request) {
        PaymentResponseDTO response = paymentService.createPaymentXendit(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/xendit-payment/webhook-create")
    @Hidden
    public ResponseEntity webhookXenditCreate(@RequestHeader("x-callback-token") String xCallbackToken,
                                              @RequestBody RequestWebhookXendit requestWebhook) {
        log.info("executing webhhook");
        String response = paymentService.createVirtualAccountXenditWebhook(xCallbackToken, requestWebhook);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/xendit-payment/webhook-paid")
    @Hidden
    public ResponseEntity webhookXenditPaid(@RequestHeader("x-callback-token") String xCallbackToken,
                                            @RequestBody RequestWebhookXenditPaid requestWebhook) {
        log.info("executing webhhook");
        String responseWebhook = paymentService.paidXenditVirtualAccountWebhook(xCallbackToken, requestWebhook);
        return ResponseEntity.ok(responseWebhook);
    }

    @GetMapping("/xendit/support-payment")
    public ResponseEntity<SupportPaymentResponse> getPaymentSupport() {
        SupportPaymentResponse response = paymentService.getSupportPayment();
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/simulate_payment", produces = "application/json")
    public ResponseEntity<Object> simulatePayment(
            @RequestParam("amount") String amount,
            @RequestParam("external_id") String externalId
    ) {
        return paymentService.simulatePayment(amount, externalId);
    }

}
