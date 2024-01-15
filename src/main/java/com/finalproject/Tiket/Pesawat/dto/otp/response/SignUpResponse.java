package com.finalproject.Tiket.Pesawat.dto.otp.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class SignUpResponse {
    private Boolean success;
    private String otp;
    private Date expiredOTP;
}
