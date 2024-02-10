package com.finalproject.Tiket.Pesawat.dto.user.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserFcmTokenRequest {

    @NotNull(message = "fcm cannot be empty")
    @Size(min = 100, message = "fcm length is not correct")
    private String fcmToken;
}
