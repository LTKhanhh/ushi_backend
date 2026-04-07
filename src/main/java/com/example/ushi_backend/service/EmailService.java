package com.example.ushi_backend.service;

public interface EmailService {
    void sendRegisterOtp(String email, String otpCode);
}
