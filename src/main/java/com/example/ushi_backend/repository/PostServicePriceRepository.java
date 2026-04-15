package com.example.ushi_backend.repository;

import com.example.ushi_backend.entity.PostServicePriceEntity;
import com.example.ushi_backend.projection.ServicePriceProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostServicePriceRepository extends JpaRepository<PostServicePriceEntity, Long> {
    @Query(value = """
            SELECT
                psp.service_type AS serviceType,
                psp.unit_price AS unitPrice,
                psp.unit_label AS unitLabel,
                psp.note AS note
            FROM post_service_prices psp
            WHERE psp.post_id = :postId
            ORDER BY psp.service_type ASC
            """, nativeQuery = true)
    List<ServicePriceProjection> findServicePricesByPostId(@Param("postId") Long postId);
}
