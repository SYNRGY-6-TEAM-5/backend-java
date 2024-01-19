package com.finalproject.Tiket.Pesawat.dto.otp;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OTPValidationRequest {
    @Email
    private String email;
    private String otp;
}
