package com.example.ushi_backend.controller;

import com.example.ushi_backend.dto.response.ApartmentCardResponse;
import jakarta.validation.constraints.Min;
import com.example.ushi_backend.dto.response.ApartmentDetailResponse;
import com.example.ushi_backend.dto.response.FavoriteToggleResponse;
import com.example.ushi_backend.model.response.PaginationResponse;
import com.example.ushi_backend.model.response.ResponseData;
import com.example.ushi_backend.service.ApartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/apartments")
public class ApartmentController {

    private final ApartmentService apartmentService;

    @GetMapping
    public ResponseData<PaginationResponse<ApartmentCardResponse>> getAllApartments(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "page must be greater than or equal to 0")
                    Integer page,
            @RequestParam(defaultValue = "20") @Min(value = 1, message = "size must be greater than 0") Integer size
    ) {
        PaginationResponse<ApartmentCardResponse> result = apartmentService.getAllApartments(extractEmail(userDetails), page, size);
        return new ResponseData<>(result, 200, "Get owner transactions successfully");
    }

    @GetMapping("/city/{cityId}")
    public ResponseData<PaginationResponse<ApartmentCardResponse>> getApartmentsByCity(
            @PathVariable Integer cityId,
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "page must be greater than or equal to 0")
                    Integer page,
            @RequestParam(defaultValue = "20") @Min(value = 1, message = "size must be greater than 0") Integer size,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        PaginationResponse<ApartmentCardResponse> data = apartmentService.getApartmentsByCity(
                extractEmail(userDetails),
                cityId,
                page,
                size
        );
        return new ResponseData<>(data, 200, "Success");
    }

    @GetMapping("/favorites")
    public ResponseData<PaginationResponse<ApartmentCardResponse>> getFavoriteApartments(
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "page must be greater than or equal to 0")
                    Integer page,
            @RequestParam(defaultValue = "20") @Min(value = 1, message = "size must be greater than 0") Integer size,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        PaginationResponse<ApartmentCardResponse> data = apartmentService.getFavoriteApartments(
                extractEmail(userDetails),
                page,
                size
        );
        return new ResponseData<>(data, 200, "Success");
    }

    @GetMapping("/recently-viewed")
    public ResponseData<PaginationResponse<ApartmentCardResponse>> getRecentlyViewedApartments(
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "page must be greater than or equal to 0")
                    Integer page,
            @RequestParam(defaultValue = "20") @Min(value = 1, message = "size must be greater than 0") Integer size,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        PaginationResponse<ApartmentCardResponse> data = apartmentService.getRecentlyViewedApartments(
                extractEmail(userDetails),
                page,
                size
        );
        return new ResponseData<>(data, 200, "Success");
    }

    @GetMapping("/{postId}")
    public ResponseData<ApartmentDetailResponse> getApartmentDetail(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        ApartmentDetailResponse data = apartmentService.getApartmentDetail(postId, extractEmail(userDetails));
        return new ResponseData<>(data, 200, "Success");
    }

    @PostMapping("/{postId}/favorite")
    public ResponseData<FavoriteToggleResponse> toggleFavorite(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        FavoriteToggleResponse data = apartmentService.toggleFavorite(postId, extractEmail(userDetails));
        return new ResponseData<>(data, 200, "Favorite updated");
    }

    @PostMapping("/{postId}/recently-viewed")
    public ResponseData<Void> markAsRecentlyViewed(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        apartmentService.markAsRecentlyViewed(postId, extractEmail(userDetails));
        return new ResponseData<>(200, "Recently viewed updated");
    }

    private String extractEmail(UserDetails userDetails) {
        return userDetails == null ? null : userDetails.getUsername();
    }
}
