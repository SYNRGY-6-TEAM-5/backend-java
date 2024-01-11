package com.finalproject.Tiket.Pesawat.dto.otp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OTPValidationRequest {
    private String email;
    private String otp;
}
