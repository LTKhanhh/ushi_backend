package com.example.ushi_backend.service;

import com.example.ushi_backend.dto.response.ApartmentCardResponse;
import com.example.ushi_backend.dto.response.ApartmentDetailResponse;
import com.example.ushi_backend.dto.response.FavoriteToggleResponse;
import com.example.ushi_backend.model.response.PaginationResponse;

public interface ApartmentService {
    PaginationResponse<ApartmentCardResponse> getAllApartments(String email, Integer page, Integer size);

    PaginationResponse<ApartmentCardResponse> getApartmentsByCity(String email, Integer cityId, Integer page, Integer size);

    PaginationResponse<ApartmentCardResponse> getFavoriteApartments(String email, Integer page, Integer size);

    PaginationResponse<ApartmentCardResponse> getRecentlyViewedApartments(String email, Integer page, Integer size);

    ApartmentDetailResponse getApartmentDetail(Long postId, String email);

    FavoriteToggleResponse toggleFavorite(Long postId, String email);

    void markAsRecentlyViewed(Long postId, String email);
}
