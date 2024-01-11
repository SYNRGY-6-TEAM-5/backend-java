package com.finalproject.Tiket.Pesawat.service;

import com.finalproject.Tiket.Pesawat.dto.otp.OTPValidationRequest;
import com.finalproject.Tiket.Pesawat.dto.otp.response.OTPValidationResponse;
import com.finalproject.Tiket.Pesawat.model.OtpInfo;

import java.util.concurrent.CompletableFuture;

public interface OTPService {
    OtpInfo generateOTP(String email);

    CompletableFuture<Boolean> sendOTPByEmail(String email, String name, String otp);

    OTPValidationResponse validateOTP(OTPValidationRequest validationRequest);
}
