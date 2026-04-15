package com.example.ushi_backend.repository;

import com.example.ushi_backend.entity.PostEntity;
import com.example.ushi_backend.projection.ApartmentCardProjection;
import com.example.ushi_backend.projection.ApartmentDetailProjection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<PostEntity, Long> {

    @Query(value = """
            SELECT
                p.id AS postId,
                pr.name AS name,
                CONCAT_WS(', ', pa.street, d.name, c.name) AS address,
                p.price AS price,
                p.package_type AS packageType,
                CASE
                    WHEN :userId IS NOT NULL AND EXISTS (
                        SELECT 1
                        FROM favorite_posts fp
                        WHERE fp.post_id = p.id
                          AND fp.user_id = :userId
                    ) THEN 1
                    ELSE 0
                END AS liked,
                (
                    SELECT pi.image_url
                    FROM post_images pi
                    WHERE pi.post_id = p.id
                    ORDER BY pi.is_thumbnail DESC, pi.sort_order ASC, pi.id ASC
                    LIMIT 1
                ) AS thumbnailImageUrl
            FROM posts p
            JOIN properties pr ON pr.id = p.property_id
            JOIN property_addresses pa ON pa.id = pr.address_id
            JOIN districts d ON d.id = pa.district_id
            JOIN cities c ON c.id = d.city_id
            WHERE p.status = 'ACTIVE'
            ORDER BY p.created_at DESC, p.id DESC
            """,
            countQuery = """
            SELECT COUNT(*)
            FROM posts p
            WHERE p.status = 'ACTIVE'
            """,
            nativeQuery = true)
    Page<ApartmentCardProjection> findApartmentCards(@Param("userId") Long userId, Pageable pageable);

    @Query(value = """
            SELECT
                p.id AS postId,
                pr.name AS name,
                CONCAT_WS(', ', pa.street, d.name, c.name) AS address,
                p.price AS price,
                p.package_type AS packageType,
                CASE
                    WHEN :userId IS NOT NULL AND EXISTS (
                        SELECT 1
                        FROM favorite_posts fp
                        WHERE fp.post_id = p.id
                          AND fp.user_id = :userId
                    ) THEN 1
                    ELSE 0
                END AS liked,
                (
                    SELECT pi.image_url
                    FROM post_images pi
                    WHERE pi.post_id = p.id
                    ORDER BY pi.is_thumbnail DESC, pi.sort_order ASC, pi.id ASC
                    LIMIT 1
                ) AS thumbnailImageUrl
            FROM posts p
            JOIN properties pr ON pr.id = p.property_id
            JOIN property_addresses pa ON pa.id = pr.address_id
            JOIN districts d ON d.id = pa.district_id
            JOIN cities c ON c.id = d.city_id
            WHERE p.status = 'ACTIVE'
              AND c.id = :cityId
            ORDER BY p.created_at DESC, p.id DESC
            """,
            countQuery = """
            SELECT COUNT(*)
            FROM posts p
            JOIN properties pr ON pr.id = p.property_id
            JOIN property_addresses pa ON pa.id = pr.address_id
            JOIN districts d ON d.id = pa.district_id
            JOIN cities c ON c.id = d.city_id
            WHERE p.status = 'ACTIVE'
              AND c.id = :cityId
            """,
            nativeQuery = true)
    Page<ApartmentCardProjection> findApartmentCardsByCity(
            @Param("cityId") Integer cityId,
            @Param("userId") Long userId,
            Pageable pageable
    );

    @Query(value = """
            SELECT
                p.id AS postId,
                pr.name AS name,
                CONCAT_WS(', ', pa.street, d.name, c.name) AS address,
                p.price AS price,
                p.package_type AS packageType,
                1 AS liked,
                (
                    SELECT pi.image_url
                    FROM post_images pi
                    WHERE pi.post_id = p.id
                    ORDER BY pi.is_thumbnail DESC, pi.sort_order ASC, pi.id ASC
                    LIMIT 1
                ) AS thumbnailImageUrl
            FROM favorite_posts fp
            JOIN posts p ON p.id = fp.post_id
            JOIN properties pr ON pr.id = p.property_id
            JOIN property_addresses pa ON pa.id = pr.address_id
            JOIN districts d ON d.id = pa.district_id
            JOIN cities c ON c.id = d.city_id
            WHERE fp.user_id = :userId
              AND p.status = 'ACTIVE'
            ORDER BY fp.created_at DESC, p.id DESC
            """,
            countQuery = """
            SELECT COUNT(*)
            FROM favorite_posts fp
            JOIN posts p ON p.id = fp.post_id
            WHERE fp.user_id = :userId
              AND p.status = 'ACTIVE'
            """,
            nativeQuery = true)
    Page<ApartmentCardProjection> findFavoriteApartmentCards(@Param("userId") Long userId, Pageable pageable);

    @Query(value = """
            SELECT
                p.id AS postId,
                pr.name AS name,
                CONCAT_WS(', ', pa.street, d.name, c.name) AS address,
                p.price AS price,
                p.package_type AS packageType,
                CASE
                    WHEN EXISTS (
                        SELECT 1
                        FROM favorite_posts fp
                        WHERE fp.post_id = p.id
                          AND fp.user_id = :userId
                    ) THEN 1
                    ELSE 0
                END AS liked,
                (
                    SELECT pi.image_url
                    FROM post_images pi
                    WHERE pi.post_id = p.id
                    ORDER BY pi.is_thumbnail DESC, pi.sort_order ASC, pi.id ASC
                    LIMIT 1
                ) AS thumbnailImageUrl
            FROM recently_viewed_posts rvp
            JOIN posts p ON p.id = rvp.post_id
            JOIN properties pr ON pr.id = p.property_id
            JOIN property_addresses pa ON pa.id = pr.address_id
            JOIN districts d ON d.id = pa.district_id
            JOIN cities c ON c.id = d.city_id
            WHERE rvp.user_id = :userId
              AND p.status = 'ACTIVE'
            ORDER BY rvp.viewed_at DESC, p.id DESC
            """,
            countQuery = """
            SELECT COUNT(*)
            FROM recently_viewed_posts rvp
            JOIN posts p ON p.id = rvp.post_id
            WHERE rvp.user_id = :userId
              AND p.status = 'ACTIVE'
            """,
            nativeQuery = true)
    Page<ApartmentCardProjection> findRecentlyViewedApartmentCards(@Param("userId") Long userId, Pageable pageable);

    @Query(value = """
            SELECT
                p.id AS postId,
                pr.name AS name,
                CONCAT_WS(', ', pa.street, d.name, c.name) AS address,
                p.price AS price,
                p.package_type AS packageType,
                CASE
                    WHEN :userId IS NOT NULL AND EXISTS (
                        SELECT 1
                        FROM favorite_posts fp
                        WHERE fp.post_id = p.id
                          AND fp.user_id = :userId
                    ) THEN TRUE
                    ELSE FALSE
                END AS liked,
                (
                    SELECT pi.image_url
                    FROM post_images pi
                    WHERE pi.post_id = p.id
                    ORDER BY pi.is_thumbnail DESC, pi.sort_order ASC, pi.id ASC
                    LIMIT 1
                ) AS thumbnailImageUrl,
                p.description AS description,
                pr.property_type AS propertyType,
                pr.area AS area,
                pr.bedrooms AS bedrooms,
                pr.bathrooms AS bathrooms,
                pr.max_people AS maxPeople,
                p.deposit_text AS depositText,
                p.payment_cycle AS paymentCycle,
                p.posted_date AS postedDate,
                p.view_count AS viewCount,
                p.favorite_count AS favoriteCount,
                p.auth AS auth,
                pa.latitude AS latitude,
                pa.longitude AS longitude,
                c.name AS city,
                d.name AS district,
                pa.street AS street,
                pa.details AS addressDetails
            FROM posts p
            JOIN properties pr ON pr.id = p.property_id
            JOIN property_addresses pa ON pa.id = pr.address_id
            JOIN districts d ON d.id = pa.district_id
            JOIN cities c ON c.id = d.city_id
            WHERE p.id = :postId
              AND p.status = 'ACTIVE'
            """, nativeQuery = true)
    Optional<ApartmentDetailProjection> findApartmentDetail(@Param("postId") Long postId, @Param("userId") Long userId);
}
