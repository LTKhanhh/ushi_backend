package com.example.ushi_backend.dto.response;

public record LandlordSummaryResponse(
        Long id,
        String name,
        String phone,
        String avatar,
        Long activePostCount
) {
}
