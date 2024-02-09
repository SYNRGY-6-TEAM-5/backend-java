package com.finalproject.Tiket.Pesawat.service;

import com.finalproject.Tiket.Pesawat.dto.PaymentResponseDTO;
import com.finalproject.Tiket.Pesawat.dto.payment.request.CreateVaPaymentRequest;
import com.finalproject.Tiket.Pesawat.dto.payment.request.RequestWebhookXendit;

public interface PaymentService {
    PaymentResponseDTO createPaymentXendit(CreateVaPaymentRequest request);

    String createVirtualAccountXenditWebhook(String xCallbackToken,RequestWebhookXendit requestWebhook);

    String paidXenditVirtualAccountWebhook(String xCallbackToken, RequestWebhookXendit requestWebhook);
}
