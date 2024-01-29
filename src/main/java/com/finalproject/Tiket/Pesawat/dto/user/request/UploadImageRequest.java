package com.finalproject.Tiket.Pesawat.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadImageRequest {
    @NotNull(message = "fileName cannot be null")
    @NotBlank(message = "fileName cannot be blank")
    private String name;
    private MultipartFile file;
}
