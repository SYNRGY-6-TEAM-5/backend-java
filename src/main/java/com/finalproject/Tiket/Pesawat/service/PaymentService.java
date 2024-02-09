package com.finalproject.Tiket.Pesawat.service;

import com.finalproject.Tiket.Pesawat.dto.PaymentResponseDTO;
import com.finalproject.Tiket.Pesawat.dto.StripeDTO;
import com.finalproject.Tiket.Pesawat.dto.payment.request.RequestWebhookXendit;
import com.stripe.exception.StripeException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface PaymentService {
    PaymentResponseDTO createPaymentXendit();

    String createVirtualAccountXenditWebhook(String xCallbackToken,RequestWebhookXendit requestWebhook);

    String paidXenditVirtualAccountWebhook(String xCallbackToken, RequestWebhookXendit requestWebhook);
}
