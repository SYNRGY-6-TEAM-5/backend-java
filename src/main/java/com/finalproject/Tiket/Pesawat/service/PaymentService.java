package com.finalproject.Tiket.Pesawat.service;

import com.finalproject.Tiket.Pesawat.dto.PaymentResponseDTO;
import com.finalproject.Tiket.Pesawat.dto.payment.request.CreateVaPaymentRequest;
import com.finalproject.Tiket.Pesawat.dto.payment.request.RequestWebhookXendit;
import com.finalproject.Tiket.Pesawat.dto.payment.request.RequestWebhookXenditPaid;
import com.finalproject.Tiket.Pesawat.dto.payment.response.SupportPaymentResponse;
import org.springframework.http.ResponseEntity;

public interface PaymentService {
    PaymentResponseDTO createPaymentXendit(CreateVaPaymentRequest request);

    String createVirtualAccountXenditWebhook(String xCallbackToken,RequestWebhookXendit requestWebhook);

    String paidXenditVirtualAccountWebhook(String xCallbackToken, RequestWebhookXenditPaid requestWebhook);

    SupportPaymentResponse getSupportPayment();

    ResponseEntity<Object> simulatePayment(String amount, String externalId);
}
