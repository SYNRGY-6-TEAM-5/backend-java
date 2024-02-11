package com.finalproject.Tiket.Pesawat.dto.auth.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestRefreshToken {
    @NotBlank(message = "Token cannot be blank or null")
    private String token;
}
