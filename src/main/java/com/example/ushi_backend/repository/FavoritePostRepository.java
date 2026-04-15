package com.example.ushi_backend.repository;

import com.example.ushi_backend.entity.FavoritePostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoritePostRepository extends JpaRepository<FavoritePostEntity, Long> {
    Optional<FavoritePostEntity> findByUserIdAndPost_Id(Long userId, Long postId);
}
