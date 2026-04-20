package com.example.ushi_backend.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ApartmentDetailResponse(
        Long postId,
        String name,
        String address,
        BigDecimal price,
        String packageType,
        Integer liked,
        String thumbnailImageUrl,
        String description,
        String propertyType,
        BigDecimal area,
        Integer bedrooms,
        Integer bathrooms,
        Integer maxPeople,
        String depositText,
        String paymentCycle,
        LocalDate postedDate,
        Long viewCount,
        Long favoriteCount,
        Boolean auth,
        BigDecimal latitude,
        BigDecimal longitude,
        String city,
        String district,
        String street,
        String addressDetails,
        LandlordSummaryResponse landlord,
        List<String> imageUrls,
        List<AmenityResponse> amenities,
        List<ServicePriceResponse> servicePrices
) {
}
