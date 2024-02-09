package com.finalproject.Tiket.Pesawat.controller;

import com.finalproject.Tiket.Pesawat.dto.PaymentResponseDTO;
import com.finalproject.Tiket.Pesawat.dto.StripeDTO;
import com.finalproject.Tiket.Pesawat.dto.payment.request.RequestWebhookXendit;
import com.finalproject.Tiket.Pesawat.service.PaymentService;
import com.stripe.exception.StripeException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Log4j2
@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/success-payment")
    public ModelAndView showSuccessPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("success");
        return modelAndView;
    }

    @GetMapping("/failed-payment")
    public ModelAndView showFailedPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("failed");
        return modelAndView;
    }

    @GetMapping("/xendit-payment/create-va")
    public ResponseEntity<PaymentResponseDTO> paymentXendit() {
        PaymentResponseDTO response = paymentService.createPaymentXendit();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/xendit-payment/webhook-create")
    public ResponseEntity webhookXenditCreate(@RequestHeader("x-callback-token") String xCallbackToken,
                                              @RequestBody RequestWebhookXendit requestWebhook) {
        log.info("executing webhhook");
        String response = paymentService.createVirtualAccountXenditWebhook(xCallbackToken, requestWebhook);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/xendit-payment/webhook-paid")
    public ResponseEntity webhookXenditPaid(@RequestHeader("x-callback-token") String xCallbackToken,
                                            @RequestBody RequestWebhookXendit requestWebhook) {
        log.info("executing webhhook");
        String responseWebhook = paymentService.paidXenditVirtualAccountWebhook(xCallbackToken, requestWebhook);
        return ResponseEntity.ok(responseWebhook);
    }
}
