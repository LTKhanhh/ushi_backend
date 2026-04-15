package com.example.ushi_backend.dto.response;

import java.math.BigDecimal;

public record ApartmentCardResponse(
        Long postId,
        String name,
        String address,
        BigDecimal price,
        String packageType,
        Integer liked,
        String thumbnailImageUrl
) {
}
