package com.finalproject.Tiket.Pesawat.service;

import com.finalproject.Tiket.Pesawat.dto.email.EmailDetails;
import com.finalproject.Tiket.Pesawat.dto.otp.OTPValidationRequest;
import com.finalproject.Tiket.Pesawat.dto.otp.response.OTPValidationResponse;
import com.finalproject.Tiket.Pesawat.exception.ExceptionHandling;
import com.finalproject.Tiket.Pesawat.exception.UnauthorizedHandling;
import com.finalproject.Tiket.Pesawat.model.OtpInfo;
import com.finalproject.Tiket.Pesawat.repository.OtpRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import static com.finalproject.Tiket.Pesawat.utils.Utils.getCurrentDateTimeAsDate;

@Log4j2
@Service
public class OTPServiceImpl implements OTPService {

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private EmailService emailService;

    private static final long OTP_EXPIRATION_DURATION = 1 * 60 * 1000; // 1 minutes

    @Override
    public OtpInfo generateOTP(String email) {
        Random random = new Random();
        int otpValue = 100_000 + random.nextInt(900_000);
        String otp = String.valueOf(otpValue);

        OtpInfo otpInfo = new OtpInfo();
        otpInfo.setEmailUser(email);
        otpInfo.setOtp(otp);
        otpInfo.setGenerateDate(getCurrentDateTimeAsDate());
        otpInfo.setExpirationDate(new Date(System.currentTimeMillis() + OTP_EXPIRATION_DURATION));

        otpRepository.save(otpInfo);
        return otpInfo;
    }


    @Override
    @Async
    public CompletableFuture<Boolean> sendOTPByEmail(String email, String name, String otp) {
        String subject = "OTP Verification";
        String msgBody = emailService.getOTPLoginEmailTemplate(email, name, otp);

        EmailDetails emailDetails = new EmailDetails();

        emailDetails.setSubject(subject);
        emailDetails.setRecipient(email);
        emailDetails.setMsgBody(msgBody);
        CompletableFuture<Void> emailSendingFuture = emailService.sendEmail(emailDetails);
        return emailSendingFuture.thenApplyAsync(result -> true).exceptionally(ex -> {
            ex.printStackTrace();
            return false;
        });
    }

    @Override
    public OTPValidationResponse validateOTP(OTPValidationRequest validationRequest) {
        Optional<OtpInfo> otpInfoOptional = otpRepository.findByEmailUser(validationRequest.getEmail());
        OTPValidationResponse response = new OTPValidationResponse();

        if (otpInfoOptional.isEmpty()) {
            throw new ExceptionHandling("OTP Not Found " + validationRequest.getEmail());
        }

        OtpInfo otpInfo = otpInfoOptional.get();

        // Check if OTP has expired
        if (otpInfo.getExpirationDate() != null && otpInfo.getExpirationDate().before(new Date())) {
            log.info("Sukses Delete OTP");
            otpRepository.delete(otpInfo);
            throw new UnauthorizedHandling("Otp Expired");
        }

        if (otpInfo.getEmailUser().equals(validationRequest.getEmail()) && otpInfo.getOtp().equals(validationRequest.getOtp())) {
            // OTP is correct
            otpRepository.delete(otpInfo);
            response.setStatus(true);
            response.setMessage("Success Validate OTP");
            return response;
        } else {
            // Wrong OTP
            throw new UnauthorizedHandling("Wrong OTP");
        }
    }

}
