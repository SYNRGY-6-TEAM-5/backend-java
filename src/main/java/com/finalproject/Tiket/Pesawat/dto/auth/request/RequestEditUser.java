package com.finalproject.Tiket.Pesawat.dto.auth.request;

import com.finalproject.Tiket.Pesawat.dto.validation.ValidPassword;
import com.finalproject.Tiket.Pesawat.dto.validation.ValidRetypePassword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ValidRetypePassword
public class RequestEditUser {
    @ValidPassword
    private String newPassword;
    @ValidPassword
    private String retypePassword;
}
