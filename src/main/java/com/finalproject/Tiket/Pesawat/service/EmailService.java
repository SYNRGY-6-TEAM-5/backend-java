package com.finalproject.Tiket.Pesawat.service;

import com.finalproject.Tiket.Pesawat.dto.email.EmailDetails;

import java.util.concurrent.CompletableFuture;

public interface EmailService {
    CompletableFuture<Void> sendEmail(EmailDetails emailDetails);
    String getForgotPasswordEmailTemplate(String email, String name, String otp);

    String getWelcomingMessageEmailTemplate(String email, String name);

    String getRegisterOtpEmailTemplate(String email, String name, String otp);
}
