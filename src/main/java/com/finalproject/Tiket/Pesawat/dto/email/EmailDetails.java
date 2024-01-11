package com.finalproject.Tiket.Pesawat.dto.email;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailDetails {
    @NotBlank
    String recipient;
    @NotBlank
    String subject;
    @NotBlank
    String msgBody;

    String attachment;

}
