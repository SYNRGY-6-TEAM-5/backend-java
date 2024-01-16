package com.finalproject.Tiket.Pesawat.dto.user.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProfileRequest {
    @NotNull(message = "Full name cannot be null")
    @NotBlank(message = "Full name cannot be blank")
    private String fullName;

    @NotNull(message = "Date of birth cannot be null")
    private Date dob;

    @Digits(integer = 15, fraction = 0, message = "Phone number must be numeric")
    @Min(value = 1000000, message = "Phone number must be at least 6 digits")
    @Max(value = 999999999999999L, message = "Phone number cannot exceed 15 digits")
    private long phoneNumber;
}
