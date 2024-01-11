package com.finalproject.Tiket.Pesawat.dto.otp.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OTPValidationResponse {
    private Boolean status;
    private String message;
}
