package com.finalproject.Tiket.Pesawat.payload.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginDto {
    @NotEmpty(message = "Email is required.")
    @NotBlank(message = "Email cannot be blank.")
    @Email
    private String emailAddress;

    @NotEmpty(message = "Password is required.")
    @NotBlank(message = "Password cannot be blank.")
    @Size(min = 8, message = "Password length min 8 character")
    private String password;
}
