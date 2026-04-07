package com.example.ushi_backend.dto.response;

public record AuthResponse(
        String message,
        UserResponse user,
        String accessToken,
        String refreshToken
) {
}
