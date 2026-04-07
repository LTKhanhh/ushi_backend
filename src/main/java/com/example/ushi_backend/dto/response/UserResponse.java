package com.example.ushi_backend.dto.response;

public record UserResponse(
        Long id,
        String email,
        String fullName,
        String phone,
        String address,
        String avatarUrl,
        String role
) {
}
