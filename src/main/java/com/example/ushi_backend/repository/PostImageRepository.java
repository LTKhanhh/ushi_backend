package com.example.ushi_backend.repository;

import com.example.ushi_backend.entity.PostImageEntity;
import com.example.ushi_backend.projection.ImageProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImageEntity, Long> {
    @Query(value = """
            SELECT pi.image_url AS imageUrl
            FROM post_images pi
            WHERE pi.post_id = :postId
            ORDER BY pi.is_thumbnail DESC, pi.sort_order ASC, pi.id ASC
            """, nativeQuery = true)
    List<ImageProjection> findImageUrlsByPostId(@Param("postId") Long postId);
}
