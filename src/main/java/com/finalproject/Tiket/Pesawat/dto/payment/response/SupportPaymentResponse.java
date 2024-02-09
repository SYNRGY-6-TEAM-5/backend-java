package com.finalproject.Tiket.Pesawat.dto.payment.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportPaymentResponse {
    private List<String> banks;
    private List<String> retails;
}
