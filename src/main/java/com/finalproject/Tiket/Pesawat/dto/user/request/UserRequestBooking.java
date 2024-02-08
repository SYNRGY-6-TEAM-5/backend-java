package com.finalproject.Tiket.Pesawat.dto.user.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestBooking {

    @NotNull(message = "Total passenger cannot be null")
    @Min(value = 1, message = "Total passenger must be at least 1")
    private Integer total_passenger;

    @NotNull(message = "Total amount cannot be null")
    @Min(value = 0, message = "Total amount cannot be negative")
    private Integer total_amount;

    @NotNull(message = "Full protection cannot be null")
    private Boolean full_protection;

    @NotNull(message = "Bag insurance cannot be null")
    private Boolean bag_insurance;

    @NotNull(message = "Flight delay cannot be null")
    private Boolean flight_delay;

    @NotNull(message = "Payment method cannot be null")
    private String payment_method;
}
