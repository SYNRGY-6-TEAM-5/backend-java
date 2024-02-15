package com.finalproject.Tiket.Pesawat.service;

import com.finalproject.Tiket.Pesawat.dto.email.EmailDetails;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.concurrent.CompletableFuture;

@Service
@Log4j2
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Override
    public CompletableFuture<Void> sendEmail(@Valid EmailDetails emailDetails) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
            messageHelper.setTo(emailDetails.getRecipient());
            messageHelper.setSubject(emailDetails.getSubject());
            messageHelper.setFrom("AeroSwift <" + senderEmail + ">");
            messageHelper.setText(emailDetails.getMsgBody(), true);
            mailSender.send(message);
            log.info("Sukses email sending");
            future.complete(null);
        } catch (MessagingException e) {
            log.error("failed sending email " + e.getCause() + " " + e.getMessage());
            future.completeExceptionally(e);
        }
        return future;
    }

    @Override
    public CompletableFuture<Void> sendEmailWithAttachment(@Valid EmailDetails emailDetails) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
            messageHelper.setTo(emailDetails.getRecipient());
            messageHelper.setSubject(emailDetails.getSubject());
            messageHelper.setFrom("AeroSwift <" + senderEmail + ">");
            messageHelper.setText(emailDetails.getMsgBody(), true);

            FileSystemResource file = new FileSystemResource(new File(emailDetails.getAttachment()));
            messageHelper.addAttachment(file.getFilename(), file);

            mailSender.send(message);
            log.info("Sukses email sending");
            future.complete(null);
        } catch (MessagingException e) {
            log.error("failed sending email " + e.getCause() + " " + e.getMessage());
            future.completeExceptionally(e);
        }
        return future;
    }


    @Override
    public String getForgotPasswordEmailTemplate(String otp) {
        String emailTemplate = "<div style=\"font-family: Helvetica, Arial, sans-serif; min-width:1000px; overflow:auto; line-height:2;\">"
                + "<div style=\"margin:50px auto; width:70%; padding:20px 0;\">"
                + "<p><img alt=\"\" src=\"https://ckeditor.com/apps/ckfinder/userfiles/files/Vector.png\" style=\"width:150px;\" /></p>"
                + "<h1 style=\"color: #00466a;\">AeroSwift</h1>"
                + "<p style=\"line-height: 1.6; margin-bottom: 15px;\">Hello User</p>"
                + "<p style=\"line-height: 1.6; margin-bottom: 15px;\">Thank you for choosing AeroSwift. Use the following OTP to complete your Forgot Password Procedure. OTP is valid for 5 minutes:</p>"
                + "<h2 style=\"background: #00466a; margin: 0 auto; width: max-content; padding: 10px; color: #fff; border-radius: 4px; text-align: center;\">" + otp + "</h2>"
                + "<p style=\"line-height: 1.6; margin-bottom: 15px;\">Regards,<br />AeroSwift</p>"
                + "<hr style=\"border: 1px solid #ddd; margin: 20px 0;\" />"
                + "<footer style=\"text-align: center; color: #888; font-size: 0.8em; margin-top: 20px;\">&copy; Aeroswift Team 2024. All rights reserved.</footer>"
                + "</div>"
                + "</div>";

        return emailTemplate;

    }

    @Override
    public String getWelcomingMessageEmailTemplate() {
        String emailTemplate = "<div style=\"font-family: Helvetica, Arial, sans-serif; min-width:1000px; overflow:auto; line-height:2;\">"
                + "<div style=\"margin:50px auto; width:70%; padding:20px 0;\">"
                + "<p><img alt=\"\" src=\"https://ckeditor.com/apps/ckfinder/userfiles/files/Vector.png\" style=\"width:150px;\" /></p>"
                + "<h1 style=\"color: #00466a;\">AeroSwift</h1>"
                + "<p style=\"line-height: 1.6; margin-bottom: 15px;\">Hello ,</p>"
                + "<p style=\"line-height: 1.6; margin-bottom: 15px;\">Welcome to AeroSwift! Thank you for choosing us. Your registration was successful.</p>"
                + "<p style=\"line-height: 1.6; margin-bottom: 15px;\">If you have any questions or need assistance, feel free to reach out to our support team.</p>"
                + "<p style=\"line-height: 1.6; margin-bottom: 15px;\">Regards,<br />AeroSwift</p>"
                + "<hr style=\"border: 1px solid #ddd; margin: 20px 0;\" />"
                + "<footer style=\"text-align: center; color: #888; font-size: 0.8em; margin-top: 20px;\">&copy; AeroSwift Team 2024. All rights reserved.</footer>"
                + "</div>"
                + "</div>";

        return emailTemplate;
    }

    @Override
    public String getRegisterOtpEmailTemplate(String email, String otp) {
        String emailTemplate = "<div style=\"font-family: Helvetica, Arial, sans-serif; min-width:1000px; overflow:auto; line-height:2;\">"
                + "<div style=\"margin:50px auto; width:70%; padding:20px 0;\">"
                + "<p><img alt=\"\" src=\"https://ckeditor.com/apps/ckfinder/userfiles/files/Vector.png\" style=\"width:150px;\" /></p>"
                + "<h1 style=\"color: #00466a;\">AeroSwift</h1>"
                + "<p style=\"line-height: 1.6; margin-bottom: 15px;\">Dear Customer</p>"
                + "<p style=\"line-height: 1.6; margin-bottom: 15px;\">Thank you for choosing AeroSwift. Use the following OTP to complete your registration. OTP is valid for 5 minutes:</p>"
                + "<h2 style=\"background: #00466a; margin: 0 auto; width: max-content; padding: 10px; color: #fff; border-radius: 4px; text-align: center;\">" + otp + "</h2>"
                + "<p style=\"line-height: 1.6; margin-bottom: 15px;\">Regards,<br />AeroSwift</p>"
                + "<hr style=\"border: 1px solid #ddd; margin: 20px 0;\" />"
                + "<footer style=\"text-align: center; color: #888; font-size: 0.8em; margin-top: 20px;\">&copy; AeroSwift Team 2024. All rights reserved.</footer>"
                + "</div>"
                + "</div>";

        return emailTemplate;
    }
}
