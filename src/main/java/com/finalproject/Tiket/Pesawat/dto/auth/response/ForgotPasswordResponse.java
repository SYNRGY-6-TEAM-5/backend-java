package com.finalproject.Tiket.Pesawat.dto.auth.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForgotPasswordResponse {
    private Boolean success;
    private String otp;
    private Date expiredOTP;
}
