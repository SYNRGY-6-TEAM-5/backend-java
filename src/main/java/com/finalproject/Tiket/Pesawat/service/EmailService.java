package com.finalproject.Tiket.Pesawat.service;

import com.finalproject.Tiket.Pesawat.dto.email.EmailDetails;

import java.util.concurrent.CompletableFuture;

public interface EmailService {
    CompletableFuture<Void> sendEmail(EmailDetails emailDetails);
    CompletableFuture<Void> sendEmailWithAttachment(EmailDetails emailDetails);
    String getForgotPasswordEmailTemplate(String otp);

    String getWelcomingMessageEmailTemplate();

    String getRegisterOtpEmailTemplate(String email, String otp);

    String getInvoiceEmailTemplate(String email);
}
