package com.finalproject.Tiket.Pesawat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StripeDTO {
    private String currency;
    private Double amount;
    private String successUrl;
    private String cancelUrl;

}
