package com.finalproject.Tiket.Pesawat.dto.auth.request;

import com.finalproject.Tiket.Pesawat.dto.validation.ValidRetypePassword;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestEditUser {
    @Size(min = 8, message = "Password length min 8 character")
    private String newPassword;
    @Size(min = 8, message = "Password length min 8 character")
    private String retypePassword;
}
