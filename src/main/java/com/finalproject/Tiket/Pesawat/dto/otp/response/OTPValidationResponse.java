package com.finalproject.Tiket.Pesawat.dto.otp.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OTPValidationResponse {
    private Boolean status;
    private String message;
    private String token;
}
