package com.example.ushi_backend.repository;

import com.example.ushi_backend.entity.PostAmenityEntity;
import com.example.ushi_backend.projection.AmenityProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostAmenityRepository extends JpaRepository<PostAmenityEntity, Long> {
    @Query(value = """
            SELECT a.id AS id, a.name AS name, a.icon AS icon
            FROM post_amenities pa
            JOIN amenities a ON a.id = pa.amenity_id
            WHERE pa.post_id = :postId
            ORDER BY a.name ASC
            """, nativeQuery = true)
    List<AmenityProjection> findAmenitiesByPostId(@Param("postId") Long postId);
}
