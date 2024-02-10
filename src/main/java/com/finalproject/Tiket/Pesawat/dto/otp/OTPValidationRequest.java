package com.finalproject.Tiket.Pesawat.dto.otp;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OTPValidationRequest {
    @Email
    private String email;
    @Size(min = 8, message = "Password length min 8 character")
    private String password;
    @NotBlank(message = "OTP cannot be empty")
    private String otp;
}
