package com.finalproject.Tiket.Pesawat.dto.user.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadFileRequest {
    private String name;
    private MultipartFile file;
}
