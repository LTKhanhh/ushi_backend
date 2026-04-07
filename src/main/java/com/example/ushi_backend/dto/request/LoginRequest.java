package com.example.ushi_backend.dto.request;

public record LoginRequest(
        String email,
        String password
) {
}
