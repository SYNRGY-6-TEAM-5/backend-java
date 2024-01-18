package com.finalproject.Tiket.Pesawat.service;

import com.finalproject.Tiket.Pesawat.dto.auth.request.ForgotPasswordRequest;
import com.finalproject.Tiket.Pesawat.dto.auth.request.SignUpRequest;
import com.finalproject.Tiket.Pesawat.dto.auth.response.ForgotPasswordResponse;
import com.finalproject.Tiket.Pesawat.dto.otp.response.SignUpResponse;
import com.finalproject.Tiket.Pesawat.exception.EmailAlreadyRegisteredHandling;
import com.finalproject.Tiket.Pesawat.exception.ExceptionHandling;
import com.finalproject.Tiket.Pesawat.model.OtpForgotPassword;
import com.finalproject.Tiket.Pesawat.model.OtpRegister;
import com.finalproject.Tiket.Pesawat.model.User;
import com.finalproject.Tiket.Pesawat.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Log4j2
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OTPService otpService;

    @Override
    public ForgotPasswordResponse forgotPasswordUser(ForgotPasswordRequest request) {
        ForgotPasswordResponse response = new ForgotPasswordResponse();
        Optional<User> userOptional = userRepository.findByEmailAddress(request.getEmail());
        if (userOptional.isEmpty()) {
            throw new ExceptionHandling("Email Not Found");
        }

        User user = userOptional.get();
        // otp send
        OtpForgotPassword otp = otpService.generateOTPForgotPassword(request.getEmail());
        CompletableFuture<Boolean> sendOtpFuture = otpService.
                sendOTPByEmailForgotPassword(request.getEmail(), user.getFullname(), otp.getOtp());
        try {

            boolean otpSent = sendOtpFuture.get();
            if (otpSent) {
                response.setSuccess(true);
                response.setOtp(otp.getOtp());
                response.setExpiredOTP(otp.getExpirationDate());
            } else {
                throw new ExceptionHandling("Failed Sending OTP");
            }

        } catch (Exception e) {
            log.error(e.getCause() + " " + e.getMessage());
        }
        return response;
    }

    @Override
    @Async
    public SignUpResponse signUpUser(SignUpRequest signUpRequest) {
        Optional<User> userOptional = userRepository.findByEmailAddress(signUpRequest.getEmail());
        if (userOptional.isPresent()) {
            throw new EmailAlreadyRegisteredHandling();
        }

        try {
            OtpRegister otpRegister = otpService.generateOTPRegister(signUpRequest.getEmail(),
                    signUpRequest.getPassword(), signUpRequest.getFullName());

            CompletableFuture<Boolean> sendOtpFuture =
                    otpService.sendOTPByEmailRegister(signUpRequest.getEmail(),
                            signUpRequest.getFullName(), otpRegister.getOtp());
            boolean otpSent = sendOtpFuture.get();
            if (otpSent) {
                return SignUpResponse.builder()
                        .success(true)
                        .otp(otpRegister.getOtp())
                        .expiredOTP(otpRegister.getExpirationDate())
                        .build();
            } else {
                throw new ExceptionHandling("Failed Sending OTP");
            }

        } catch (Exception e) {
            throw new ExceptionHandling("Failed Create User");
        }
    }

}
