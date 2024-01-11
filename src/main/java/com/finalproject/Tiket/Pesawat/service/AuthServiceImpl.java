package com.finalproject.Tiket.Pesawat.service;

import com.finalproject.Tiket.Pesawat.dto.auth.request.ForgotPasswordRequest;
import com.finalproject.Tiket.Pesawat.dto.auth.response.ForgotPasswordResponse;
import com.finalproject.Tiket.Pesawat.exception.ExceptionHandling;
import com.finalproject.Tiket.Pesawat.exception.UnauthorizedHandling;
import com.finalproject.Tiket.Pesawat.model.OtpInfo;
import com.finalproject.Tiket.Pesawat.model.User;
import com.finalproject.Tiket.Pesawat.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
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
        OtpInfo otp = otpService.generateOTP(request.getEmail());
        CompletableFuture<Boolean> sendOtpFuture = otpService.sendOTPByEmail(
                request.getEmail(), user.getFullname(), otp.getOtp());
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
}
