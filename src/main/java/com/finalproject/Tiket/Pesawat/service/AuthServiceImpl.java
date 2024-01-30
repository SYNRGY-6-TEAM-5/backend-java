package com.finalproject.Tiket.Pesawat.service;

import com.finalproject.Tiket.Pesawat.dto.auth.request.ForgotPasswordRequest;
import com.finalproject.Tiket.Pesawat.dto.auth.request.RequestEditUser;
import com.finalproject.Tiket.Pesawat.dto.auth.request.SignUpRequest;
import com.finalproject.Tiket.Pesawat.dto.auth.response.ForgotPasswordResponse;
import com.finalproject.Tiket.Pesawat.dto.auth.response.ResponseEditPassword;
import com.finalproject.Tiket.Pesawat.dto.otp.response.SignUpResponse;
import com.finalproject.Tiket.Pesawat.exception.EmailAlreadyRegisteredHandling;
import com.finalproject.Tiket.Pesawat.exception.ExceptionHandling;
import com.finalproject.Tiket.Pesawat.exception.UnauthorizedHandling;
import com.finalproject.Tiket.Pesawat.model.OtpForgotPassword;
import com.finalproject.Tiket.Pesawat.model.OtpRegister;
import com.finalproject.Tiket.Pesawat.model.User;
import com.finalproject.Tiket.Pesawat.repository.UserRepository;
import com.finalproject.Tiket.Pesawat.security.service.UserDetailsImpl;
import com.finalproject.Tiket.Pesawat.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.finalproject.Tiket.Pesawat.utils.Constants.CONSTANT_EMAIL_TEST_FORGOT;

@Service
@Log4j2
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private UserRepository userRepository;

    private OTPService otpService;

    private PasswordEncoder passwordEncoder;


    @Override
    public ForgotPasswordResponse forgotPasswordUser(ForgotPasswordRequest request) {
        ForgotPasswordResponse response = new ForgotPasswordResponse();
        log.info(request.getEmail() +  " dann " + CONSTANT_EMAIL_TEST_FORGOT);
        Optional<User> userOptional = userRepository.findByEmailAddress(request.getEmail());
        if (userOptional.isEmpty() && !request.getEmail().equals(CONSTANT_EMAIL_TEST_FORGOT)) {
            throw new ExceptionHandling("Email Not Found");
        }

        // otp send
        OtpForgotPassword otp = otpService.generateOTPForgotPassword(request.getEmail());
        CompletableFuture<Boolean> sendOtpFuture = otpService.
                sendOTPByEmailForgotPassword(request.getEmail(), otp.getOtp());
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
        if (userOptional.isPresent() && !signUpRequest.getEmail().equals("testAeroSwift@gmail.com")) {
            throw new EmailAlreadyRegisteredHandling();
        }

        try {
            OtpRegister otpRegister = otpService.generateOTPRegister(signUpRequest.getEmail(),
                    signUpRequest.getPassword());

            CompletableFuture<Boolean> sendOtpFuture =
                    otpService.sendOTPByEmailRegister(signUpRequest.getEmail(),
                            otpRegister.getOtp());
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
            log.error(e.getMessage());
            throw new UnauthorizedHandling("An OTP has already been sent to your email. " +
                    "Please check your inbox before requesting a new one.");
        }
    }

    @Override
    public ResponseEditPassword editPassUser(RequestEditUser requestEditUser) {
        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = authentication.getPrincipal();



            if (principal instanceof UserDetailsImpl) {
                Optional<User> userOptional = userRepository
                        .findByEmailAddress(((UserDetailsImpl) principal).getUsername());
                if (userOptional.isEmpty()) {
                    throw new UnauthorizedHandling("User Not Found");
                }
                User user = userOptional.get();

                if (!requestEditUser.getNewPassword().equals(requestEditUser.getRetypePassword())) {
                    throw new UnauthorizedHandling("New Password and Retype Password do not match");
                }

                String newPassword = passwordEncoder.encode(requestEditUser.getNewPassword());

                user.setPassword(newPassword);
                user.setLastModified(Utils.getCurrentDateTimeAsDate());
                userRepository.save(user);
            } else if (principal instanceof String) {
                throw new UnauthorizedHandling("User not authenticated");
            }

        } catch (Exception e) {
            log.error(e.getCause() + " " + e.getMessage());
            throw new ExceptionHandling(e.getMessage());
        }


        return ResponseEditPassword.builder()
                .status(true)
                .message("Success Update User Password")
                .build();
    }

}
