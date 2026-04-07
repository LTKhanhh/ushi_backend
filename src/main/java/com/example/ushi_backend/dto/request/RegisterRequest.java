package com.example.ushi_backend.dto.request;

public record RegisterRequest(
        String email,
        String password,
        String otp
) {
}
