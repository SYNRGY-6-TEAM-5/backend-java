package com.finalproject.Tiket.Pesawat.service;

import com.finalproject.Tiket.Pesawat.dto.auth.response.ValidSignUpResponse;
import com.finalproject.Tiket.Pesawat.dto.email.EmailDetails;
import com.finalproject.Tiket.Pesawat.dto.otp.OTPValidationRequest;
import com.finalproject.Tiket.Pesawat.dto.otp.response.OTPValidationResponse;
import com.finalproject.Tiket.Pesawat.exception.ExceptionHandling;
import com.finalproject.Tiket.Pesawat.exception.UnauthorizedHandling;
import com.finalproject.Tiket.Pesawat.model.*;
import com.finalproject.Tiket.Pesawat.repository.OtpForgotPasswordRepository;
import com.finalproject.Tiket.Pesawat.repository.OtpRegisterRepository;
import com.finalproject.Tiket.Pesawat.repository.RoleRepository;
import com.finalproject.Tiket.Pesawat.repository.UserRepository;
import com.finalproject.Tiket.Pesawat.utils.Utils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private OtpForgotPasswordRepository otpForgotPasswordRepository;

    @Autowired
    private OtpRegisterRepository otpRegisterRepository;
    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    private static final long OTP_EXPIRATION_DURATION = 1 * 60 * 1000; // 1 minutes // todo discuss with FE

    @Override
    public OtpForgotPassword generateOTPForgotPassword(String email) {
        Random random = new Random();
        int otpValue = 100_000 + random.nextInt(900_000);
        String otp = String.valueOf(otpValue);

        OtpForgotPassword otpForgotPassword = new OtpForgotPassword();
        otpForgotPassword.setEmailUser(email);
        otpForgotPassword.setOtp(otp);
        otpForgotPassword.setGenerateDate(getCurrentDateTimeAsDate());
        otpForgotPassword.setExpirationDate(new Date(System.currentTimeMillis() + OTP_EXPIRATION_DURATION));

        otpForgotPasswordRepository.save(otpForgotPassword);
        return otpForgotPassword;
    }


    @Override
    @Async
    public CompletableFuture<Boolean> sendOTPByEmailForgotPassword(String email, String name, String otp) {
        String subject = "OTP Verification";
        String msgBody = emailService.getForgotPasswordEmailTemplate(email, name, otp);

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
    public OTPValidationResponse validateOTPForgotPassword(OTPValidationRequest validationRequest) {
        Optional<OtpForgotPassword> otpInfoOptional = otpForgotPasswordRepository.findByEmailUser(validationRequest.getEmail());
        OTPValidationResponse response = new OTPValidationResponse();

        if (otpInfoOptional.isEmpty()) {
            throw new ExceptionHandling("OTP Not Found " + validationRequest.getEmail());
        }

        OtpForgotPassword otpForgotPassword = otpInfoOptional.get();

        // Check if OTP has expired
        if (otpForgotPassword.getExpirationDate() != null && otpForgotPassword.getExpirationDate().before(new Date())) {
            log.info("Sukses Delete OTP");
            otpForgotPasswordRepository.delete(otpForgotPassword);
            throw new UnauthorizedHandling("Otp Expired");
        }

        if (otpForgotPassword.getEmailUser().equals(validationRequest.getEmail()) && otpForgotPassword.getOtp().equals(validationRequest.getOtp())) {
            // OTP is correct
            otpForgotPasswordRepository.delete(otpForgotPassword);
            response.setStatus(true);
            response.setMessage("Success Validate OTP");
            return response;
        } else {
            // Wrong OTP
            throw new UnauthorizedHandling("Wrong OTP");
        }
    }

    @Override
    public OtpRegister generateOTPRegister(String email, String password, String fullName) {
        Random random = new Random();
        int otpValue = 100_000 + random.nextInt(900_000);
        String otp = String.valueOf(otpValue);
        String encryptedPassword = passwordEncoder.encode(password);

        OtpRegister otpRegister = OtpRegister.builder()
                .emailUser(email)
                .otp(otp)
                .fullName(fullName)
                .password(encryptedPassword)
                .generateDate(getCurrentDateTimeAsDate())
                .expirationDate(new Date(System.currentTimeMillis() + OTP_EXPIRATION_DURATION))
                .build();

        otpRegisterRepository.save(otpRegister);
        return otpRegister;
    }


    @Override
    public CompletableFuture<Boolean> sendOTPByEmailRegister(String email, String name, String otp) {
        String subject = "OTP Verification";
        String msgBody = emailService.getRegisterOtpEmailTemplate(email, name, otp);

        EmailDetails emailDetails = EmailDetails.builder()
                .subject(subject)
                .recipient(email)
                .msgBody(msgBody)
                .build();

        CompletableFuture<Void> emailSendingFuture = emailService.sendEmail(emailDetails);
        return emailSendingFuture.thenApplyAsync(result -> true).exceptionally(ex -> {
            ex.printStackTrace();
            return false;
        });
    }

    @Override
    public ValidSignUpResponse validateOTPRegister(OTPValidationRequest validationRequest) {
        Optional<OtpRegister> otpInfoOptional = otpRegisterRepository.findByEmailUser(validationRequest.getEmail());

        if (otpInfoOptional.isEmpty()) {
            throw new ExceptionHandling("OTP Not Found " + validationRequest.getEmail());
        }

        OtpRegister otpRegister = otpInfoOptional.get();

        if (otpRegister.getExpirationDate() != null && otpRegister.getExpirationDate().before(new Date())) {
            log.info("Sukses Delete OTP");
            otpRegisterRepository.delete(otpRegister);
            throw new UnauthorizedHandling("Otp Expired");
        }

        if (otpRegister.getEmailUser().equals(validationRequest.getEmail())
                && otpRegister.getOtp().equals(validationRequest.getOtp())) {

            Role userRole = roleRepository.findByRoleName(EnumRole.USER).orElseGet(() -> {
                Role newRole = new Role();
                newRole.setRoleName(EnumRole.USER);
                return roleRepository.save(newRole);
            });

            // SAVE USER HERE INTO DB
            User newUser = User.builder()
                    .fullname(otpRegister.getFullName())
                    .emailAddress(otpRegister.getEmailUser())
                    .password(otpRegister.getPassword())
                    .isActive(true)
                    .role(userRole)
                    .createdAt(Utils.getCurrentDateTimeAsDate()).build();

            userRepository.save(newUser);

            // WELCOMING MESSAGE HERE
            String subject = "Welcome User";
            String msgBody = emailService.getWelcomingMessageEmailTemplate(
                    otpRegister.getEmailUser(), otpRegister.getFullName()
            );

            EmailDetails emailDetails = EmailDetails.builder()
                    .subject(subject)
                    .msgBody(msgBody)
                    .recipient(otpRegister.getEmailUser())
                    .build();

            CompletableFuture<Void> emailSendingFuture = emailService.sendEmail(emailDetails);
            emailSendingFuture.thenApplyAsync(result -> true).exceptionally(ex -> {
                ex.printStackTrace();
                return false;
            });

            otpRegisterRepository.delete(otpRegister);
            return ValidSignUpResponse.builder()
                    .success(true)
                    .message("Success create user")
                    .build();
        } else {
            // Wrong OTP
            throw new UnauthorizedHandling("Wrong OTP");
        }
    }

}
