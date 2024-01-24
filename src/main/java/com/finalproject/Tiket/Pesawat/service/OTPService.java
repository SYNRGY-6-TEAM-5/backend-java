package com.finalproject.Tiket.Pesawat.service;

import com.finalproject.Tiket.Pesawat.dto.auth.response.ValidSignUpResponse;
import com.finalproject.Tiket.Pesawat.dto.otp.OTPValidationRequest;
import com.finalproject.Tiket.Pesawat.dto.otp.response.OTPValidationResponse;
import com.finalproject.Tiket.Pesawat.model.OtpForgotPassword;
import com.finalproject.Tiket.Pesawat.model.OtpRegister;

import java.util.concurrent.CompletableFuture;

public interface OTPService {
    OtpForgotPassword generateOTPForgotPassword(String email);

    CompletableFuture<Boolean> sendOTPByEmailForgotPassword(String email, String otp);

    OTPValidationResponse validateOTPForgotPassword(OTPValidationRequest validationRequest);

    OtpRegister generateOTPRegister(String email, String password);

    CompletableFuture<Boolean> sendOTPByEmailRegister(String email,String otp);

    ValidSignUpResponse validateOTPRegister(OTPValidationRequest validationRequest);
}
