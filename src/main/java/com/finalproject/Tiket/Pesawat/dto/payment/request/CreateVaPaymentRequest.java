package com.finalproject.Tiket.Pesawat.dto.payment.request;

import com.finalproject.Tiket.Pesawat.dto.validation.BankCodeValidation;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateVaPaymentRequest {
    @Min(value = 1, message = "Booking ID must be at least 1")
    private Integer bookingId;

    @BankCodeValidation
    private String bankCode;
}
