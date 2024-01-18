package com.finalproject.Tiket.Pesawat.dto.user.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadFileResponse {
    private Boolean success;
    private String message;
    private String urlImage;
}
