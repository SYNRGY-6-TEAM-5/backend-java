package com.finalproject.Tiket.Pesawat.service;

import com.finalproject.Tiket.Pesawat.dto.email.EmailDetails;

import java.util.concurrent.CompletableFuture;

public interface EmailService {
    CompletableFuture<Void> sendEmail(EmailDetails emailDetails);
    String getForgotPasswordEmailTemplate(String name, String otp);

    String getWelcomingMessageEmailTemplate(String name);

    String getRegisterOtpEmailTemplate(String email, String name, String otp);

    String getForgotSendPassword(String emailAddress, String newPassword);
}
