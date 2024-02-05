package com.finalproject.Tiket.Pesawat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SuccesMessageDTO {
    private Boolean success;
    private String message;
}
