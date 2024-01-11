package com.finalproject.Tiket.Pesawat.service;

import com.finalproject.Tiket.Pesawat.dto.auth.request.ForgotPasswordRequest;
import com.finalproject.Tiket.Pesawat.dto.auth.response.ForgotPasswordResponse;

public interface AuthService {

    ForgotPasswordResponse forgotPasswordUser(ForgotPasswordRequest request);
}
