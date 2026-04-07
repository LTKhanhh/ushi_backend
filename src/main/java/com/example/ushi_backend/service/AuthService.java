package com.example.ushi_backend.service;

import com.example.ushi_backend.dto.request.LoginRequest;
import com.example.ushi_backend.dto.request.RequestRegisterOtpRequest;
import com.example.ushi_backend.dto.request.RefreshTokenRequest;
import com.example.ushi_backend.dto.request.RegisterRequest;
import com.example.ushi_backend.dto.response.ApiResponse;
import com.example.ushi_backend.dto.response.AuthResponse;

public interface AuthService {
    ApiResponse requestRegisterOtp(RequestRegisterOtpRequest request);

    ApiResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refresh(RefreshTokenRequest request);

    ApiResponse logout(RefreshTokenRequest request);
}
