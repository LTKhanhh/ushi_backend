package com.example.ushi_backend.service.implement;

import com.example.ushi_backend.dto.response.AmenityResponse;
import com.example.ushi_backend.dto.response.ApartmentCardResponse;
import com.example.ushi_backend.dto.response.ApartmentDetailResponse;
import com.example.ushi_backend.dto.response.FavoriteToggleResponse;
import com.example.ushi_backend.dto.response.LandlordSummaryResponse;
import com.example.ushi_backend.dto.response.ServicePriceResponse;
import com.example.ushi_backend.entity.FavoritePostEntity;
import com.example.ushi_backend.entity.PostEntity;
import com.example.ushi_backend.entity.RecentlyViewedPostEntity;
import com.example.ushi_backend.entity.UserEntity;
import com.example.ushi_backend.exception.ResourceNotFoundException;
import com.example.ushi_backend.exception.UnauthorizedException;
import com.example.ushi_backend.projection.ApartmentCardProjection;
import com.example.ushi_backend.projection.ApartmentDetailProjection;
import com.example.ushi_backend.repository.FavoritePostRepository;
import com.example.ushi_backend.repository.PostAmenityRepository;
import com.example.ushi_backend.repository.PostImageRepository;
import com.example.ushi_backend.repository.PostRepository;
import com.example.ushi_backend.repository.PostServicePriceRepository;
import com.example.ushi_backend.repository.RecentlyViewedPostRepository;
import com.example.ushi_backend.repository.UserRepository;
import com.example.ushi_backend.service.ApartmentService;

import com.example.ushi_backend.exception.ValidationException;
import com.example.ushi_backend.model.response.PaginationResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApartmentServiceImpl implements ApartmentService {

    private final PostRepository postRepository;
    private final FavoritePostRepository favoritePostRepository;
    private final RecentlyViewedPostRepository recentlyViewedPostRepository;
    private final PostImageRepository postImageRepository;
    private final PostAmenityRepository postAmenityRepository;
    private final PostServicePriceRepository postServicePriceRepository;
    private final UserRepository userRepository;

    @Override
    public PaginationResponse<ApartmentCardResponse> getAllApartments(String email, Integer page, Integer size) {

        int safePage = page == null ? 0 : page;
        int safeSize = size == null ? 20 : size;

        validatePagination(safePage, safeSize);

        Pageable pageable = PageRequest.of(safePage, safeSize);
        Long userId = findOptionalUserId(email);
        Page<ApartmentCardProjection> apartmentPage = postRepository.findApartmentCards(userId, pageable);
        return PaginationResponse.<ApartmentCardResponse>builder()
                .data(apartmentPage.map(this::toApartmentCardResponse).getContent())
                .page(apartmentPage.getNumber())
                .limit(apartmentPage.getSize())
                .totalPage(apartmentPage.getTotalPages())
                .totalResult(apartmentPage.getTotalElements())
                .build();
    }

    @Override
    public PaginationResponse<ApartmentCardResponse> getApartmentsByCity(String email, Integer cityId, Integer page, Integer size) {
        int safePage = page == null ? 0 : page;
        int safeSize = size == null ? 20 : size;

        validatePagination(safePage, safeSize);

        Pageable pageable = PageRequest.of(safePage, safeSize);
        Long userId = findOptionalUserId(email);
        Page<ApartmentCardProjection> apartmentPage = postRepository.findApartmentCardsByCity(cityId, userId, pageable);
        return PaginationResponse.<ApartmentCardResponse>builder()
                .data(apartmentPage.map(this::toApartmentCardResponse).getContent())
                .page(apartmentPage.getNumber())
                .limit(apartmentPage.getSize())
                .totalPage(apartmentPage.getTotalPages())
                .totalResult(apartmentPage.getTotalElements())
                .build();
    }

    @Override
    public PaginationResponse<ApartmentCardResponse> getFavoriteApartments(String email, Integer page, Integer size) {
        int safePage = page == null ? 0 : page;
        int safeSize = size == null ? 20 : size;

        validatePagination(safePage, safeSize);

        Pageable pageable = PageRequest.of(safePage, safeSize);
        Long userId = findRequiredUserId(email);
        Page<ApartmentCardProjection> apartmentPage = postRepository.findFavoriteApartmentCards(userId, pageable);
        return PaginationResponse.<ApartmentCardResponse>builder()
                .data(apartmentPage.map(this::toApartmentCardResponse).getContent())
                .page(apartmentPage.getNumber())
                .limit(apartmentPage.getSize())
                .totalPage(apartmentPage.getTotalPages())
                .totalResult(apartmentPage.getTotalElements())
                .build();
    }

    @Override
    public PaginationResponse<ApartmentCardResponse> getRecentlyViewedApartments(String email, Integer page, Integer size) {
        int safePage = page == null ? 0 : page;
        int safeSize = size == null ? 20 : size;

        validatePagination(safePage, safeSize);

        Pageable pageable = PageRequest.of(safePage, safeSize);
        Long userId = findRequiredUserId(email);
        Page<ApartmentCardProjection> apartmentPage = postRepository.findRecentlyViewedApartmentCards(userId, pageable);
        return PaginationResponse.<ApartmentCardResponse>builder()
                .data(apartmentPage.map(this::toApartmentCardResponse).getContent())
                .page(apartmentPage.getNumber())
                .limit(apartmentPage.getSize())
                .totalPage(apartmentPage.getTotalPages())
                .totalResult(apartmentPage.getTotalElements())
                .build();
    }

    @Override
    public ApartmentDetailResponse getApartmentDetail(Long postId, String email) {
        Long userId = findOptionalUserId(email);
        ApartmentDetailProjection detail = postRepository.findApartmentDetail(postId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay can ho"));

        List<String> imageUrls = postImageRepository.findImageUrlsByPostId(postId).stream()
                .map(image -> image.getImageUrl())
                .toList();

        List<AmenityResponse> amenities = postAmenityRepository.findAmenitiesByPostId(postId).stream()
                .map(amenity -> new AmenityResponse(amenity.getId(), amenity.getName(), amenity.getIcon()))
                .toList();

        List<ServicePriceResponse> servicePrices = postServicePriceRepository.findServicePricesByPostId(postId).stream()
                .map(servicePrice -> new ServicePriceResponse(
                        servicePrice.getServiceType(),
                        servicePrice.getUnitPrice(),
                        servicePrice.getUnitLabel(),
                        servicePrice.getNote()
                ))
                .toList();

        return new ApartmentDetailResponse(
                detail.getPostId(),
                detail.getName(),
                detail.getAddress(),
                detail.getPrice(),
                detail.getPackageType(),
                detail.getLiked(),
                detail.getThumbnailImageUrl(),
                detail.getDescription(),
                detail.getPropertyType(),
                detail.getArea(),
                detail.getBedrooms(),
                detail.getBathrooms(),
                detail.getMaxPeople(),
                detail.getDepositText(),
                detail.getPaymentCycle(),
                detail.getPostedDate(),
                detail.getViewCount(),
                detail.getFavoriteCount(),
                detail.getAuth(),
                detail.getLatitude(),
                detail.getLongitude(),
                detail.getCity(),
                detail.getDistrict(),
                detail.getStreet(),
                detail.getAddressDetails(),
                new LandlordSummaryResponse(
                        detail.getLandlordId(),
                        detail.getLandlordName(),
                        detail.getLandlordPhone(),
                        detail.getLandlordAvatar(),
                        detail.getLandlordActivePostCount()
                ),
                imageUrls,
                amenities,
                servicePrices
        );
    }

    @Override
    @Transactional
    public FavoriteToggleResponse toggleFavorite(Long postId, String email) {
        Long userId = findRequiredUserId(email);
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay can ho"));

        Optional<FavoritePostEntity> existingFavorite = favoritePostRepository.findByUserIdAndPost_Id(userId, postId);
        boolean liked;

        if (existingFavorite.isPresent()) {
            favoritePostRepository.delete(existingFavorite.get());
            post.setFavoriteCount(Math.max(0L, defaultLong(post.getFavoriteCount()) - 1L));
            liked = false;
        } else {
            FavoritePostEntity favoritePost = new FavoritePostEntity();
            favoritePost.setUserId(userId);
            favoritePost.setPost(post);
            favoritePostRepository.save(favoritePost);
            post.setFavoriteCount(defaultLong(post.getFavoriteCount()) + 1L);
            liked = true;
        }

        postRepository.save(post);
        return new FavoriteToggleResponse(postId, liked);
    }

    @Override
    @Transactional
    public void markAsRecentlyViewed(Long postId, String email) {
        Long userId = findRequiredUserId(email);
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay can ho"));

        RecentlyViewedPostEntity recentlyViewedPost = recentlyViewedPostRepository.findByUserIdAndPost_Id(userId, postId)
                .orElseGet(() -> {
                    RecentlyViewedPostEntity entity = new RecentlyViewedPostEntity();
                    entity.setUserId(userId);
                    entity.setPost(post);
                    return entity;
                });

        recentlyViewedPost.setViewedAt(LocalDateTime.now());
        recentlyViewedPostRepository.save(recentlyViewedPost);

        post.setViewCount(defaultLong(post.getViewCount()) + 1L);
        postRepository.save(post);
    }

    private ApartmentCardResponse toApartmentCardResponse(ApartmentCardProjection projection) {
        return new ApartmentCardResponse(
                projection.getPostId(),
                projection.getName(),
                projection.getAddress(),
                projection.getPrice(),
                projection.getPackageType(),
                projection.getLiked(),
                projection.getThumbnailImageUrl()
        );
    }

    private Long findOptionalUserId(String email) {
        if (email == null || email.isBlank()) {
            return null;
        }
        return userRepository.findByEmail(email)
                .map(UserEntity::getId)
                .orElse(null);
    }

    private Long findRequiredUserId(String email) {
        Long userId = findOptionalUserId(email);
        if (userId == null) {
            throw new UnauthorizedException("Ban can dang nhap de thuc hien thao tac nay");
        }
        return userId;
    }

    private long defaultLong(Long value) {
        return value == null ? 0L : value;
    }

    private void validatePagination(int page, int size) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();

        if (page < 0) {
            fieldErrors.put("page", "page must be greater than or equal to 0");
        }
        if (size <= 0) {
            fieldErrors.put("size", "size must be greater than 0");
        }

        if (!fieldErrors.isEmpty()) {
            throw ValidationException.of("INVALID_PAGINATION", fieldErrors);
        }
    }
}
