package com.finalproject.Tiket.Pesawat.service;

import com.finalproject.Tiket.Pesawat.dto.auth.request.ForgotPasswordRequest;
import com.finalproject.Tiket.Pesawat.dto.auth.request.RequestEditUser;
import com.finalproject.Tiket.Pesawat.dto.auth.request.SignUpRequest;
import com.finalproject.Tiket.Pesawat.dto.auth.response.ForgotPasswordResponse;
import com.finalproject.Tiket.Pesawat.dto.auth.response.ResponseEditPassword;
import com.finalproject.Tiket.Pesawat.dto.otp.response.SignUpResponse;

public interface AuthService {
    ForgotPasswordResponse forgotPasswordUser(ForgotPasswordRequest request);
    SignUpResponse signUpUser(SignUpRequest signUpRequest);

    ResponseEditPassword editPassUser(RequestEditUser requestEditUser);
}
