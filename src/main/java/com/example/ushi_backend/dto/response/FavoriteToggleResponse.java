package com.example.ushi_backend.dto.response;

public record FavoriteToggleResponse(
        Long postId,
        Boolean liked
) {
}
