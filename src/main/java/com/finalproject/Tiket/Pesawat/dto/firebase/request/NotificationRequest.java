package com.finalproject.Tiket.Pesawat.dto.firebase.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationRequest {
    private String title;
    private String body;
    private String topic;
    private String token;
}
