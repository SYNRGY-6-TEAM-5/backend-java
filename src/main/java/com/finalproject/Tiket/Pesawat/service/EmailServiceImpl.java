package com.finalproject.Tiket.Pesawat.service;

import com.finalproject.Tiket.Pesawat.dto.email.EmailDetails;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

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
            messageHelper.setFrom("E-Flight <" + senderEmail + ">");
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
    public String getForgotPasswordEmailTemplate(String email, String name, String otp) {
        String emailTemplate = "<div style=\"font-family: Helvetica,Arial,sans-serif;min-width:1000px;overflow:auto;line-height:2\">"
                + "<div style=\"margin:50px auto;width:70%;padding:20px 0\">"
                + "<div style=\"border-bottom:1px solid #eee\">"
                + "<a href=\"https://piggybank.netlify.app/\" style=\"font-size:1.4em;color: #00466a;text-decoration:none;font-weight:600\">piggybank</a>"
                + "</div>"
                + "<p style=\"font-size:1.1em\">Hi, " + name + "</p>"
                + "<p style=\"font-size:0.9em;\">Email: " + email + "</p>"
                + "<p>Thank you for choosing OneStopBank. Use the following OTP to complete your Log In procedures. OTP is valid for 3 minutes</p>"
                + "<h2 style=\"background: #00466a;margin: 0 auto;width: max-content;padding: 0 10px;color: #fff;border-radius: 4px;\">" + otp + "</h2>"
                + "<p style=\"font-size:0.9em;\">Regards,<br />OneStopBank</p>"
                + "<hr style=\"border:none;border-top:1px solid #eee\" />"
                + "<p>piggybank Inc</p>"
                + "<p>1600 Amphitheatre Parkway</p>"
                + "<p>California</p>"
                + "</div>"
                + "</div>";

        return emailTemplate;

    }

    @Override
    public String getWelcomingMessageEmailTemplate(String email, String name) {
        String emailTemplate = "<div style=\"font-family: Helvetica,Arial,sans-serif;min-width:1000px;overflow:auto;line-height:2\">"
                + "<div style=\"margin:50px auto;width:70%;padding:20px 0\">"
                + "<div style=\"border-bottom:1px solid #eee\">"
                + "<a href=\"https://piggybank.netlify.app/\" style=\"font-size:1.4em;color: #00466a;text-decoration:none;font-weight:600\">piggybank</a>"
                + "</div>"
                + "<p style=\"font-size:1.1em\">Hi, " + name + "</p>"
                + "<p style=\"font-size:0.9em;\">Email: " + email + "</p>"
                + "<p>Thank you for choosing OneStopBank. Use the following OTP to complete your Log In procedures. OTP is valid for 3 minutes</p>"
                + "<h2 style=\"background: #00466a;margin: 0 auto;width: max-content;padding: 0 10px;color: #fff;border-radius: 4px;\">" + "</h2>"
                + "<p style=\"font-size:0.9em;\">Regards,<br />OneStopBank</p>"
                + "<hr style=\"border:none;border-top:1px solid #eee\" />"
                + "<p>piggybank Inc</p>"
                + "<p>1600 Amphitheatre Parkway</p>"
                + "<p>California</p>"
                + "</div>"
                + "</div>";

        return emailTemplate;
    }

    @Override
    public String getRegisterOtpEmailTemplate(String email, String name, String otp) {
        String emailTemplate = "<div style=\"font-family: Helvetica,Arial,sans-serif;min-width:1000px;overflow:auto;line-height:2\">"
                + "<div style=\"margin:50px auto;width:70%;padding:20px 0\">"
                + "<div style=\"border-bottom:1px solid #eee\">"
                + "<a href=\"https://piggybank.netlify.app/\" style=\"font-size:1.4em;color: #00466a;text-decoration:none;font-weight:600\">piggybank</a>"
                + "</div>"
                + "<p style=\"font-size:1.1em\">Hi, " + name + "</p>"
                + "<p style=\"font-size:0.9em;\">Email: " + email + "</p>"
                + "<p>Thank you for choosing OneStopBank. Use the following OTP to complete your Log In procedures. OTP is valid for 3 minutes</p>"
                + "<h2 style=\"background: #00466a;margin: 0 auto;width: max-content;padding: 0 10px;color: #fff;border-radius: 4px;\">" + "</h2>"
                + "<p style=\"font-size:0.9em;\">Regards,<br />OneStopBank</p>"
                + "<hr style=\"border:none;border-top:1px solid #eee\" />"
                + "<p>piggybank Inc</p>"
                + "<p>1600 Amphitheatre Parkway</p>"
                + "<p>California</p>"
                + "</div>"
                + "</div>";

        return emailTemplate;
    }
}
