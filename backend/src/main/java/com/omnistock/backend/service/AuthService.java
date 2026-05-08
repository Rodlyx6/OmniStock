package com.omnistock.backend.service;

import com.omnistock.backend.domain.dto.AuthResponse;
import com.omnistock.backend.domain.dto.ForgotPasswordRequest;
import com.omnistock.backend.domain.dto.LoginRequest;
import com.omnistock.backend.domain.dto.RegisterRequest;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    void register(RegisterRequest request);
    void forgotPassword(ForgotPasswordRequest request);
}
