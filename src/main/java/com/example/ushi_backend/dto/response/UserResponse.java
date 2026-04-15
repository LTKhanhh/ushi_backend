package com.example.ushi_backend.dto.response;

import com.example.ushi_backend.entity.AccountAddressEntity;

public record UserResponse(
        Long id,
        String email,
        String fullName,
        String phone,
        AccountAddressEntity address,
        String avatarUrl,
        String role
) {
}
