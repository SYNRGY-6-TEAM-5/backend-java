package com.finalproject.Tiket.Pesawat.dto.auth.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidSignUpResponse {
    private Boolean success;
    private String message;
}
