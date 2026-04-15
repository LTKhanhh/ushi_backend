package com.example.ushi_backend.repository;

import com.example.ushi_backend.entity.RecentlyViewedPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecentlyViewedPostRepository extends JpaRepository<RecentlyViewedPostEntity, Long> {
    Optional<RecentlyViewedPostEntity> findByUserIdAndPost_Id(Long userId, Long postId);
}
