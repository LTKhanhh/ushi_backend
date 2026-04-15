package com.example.ushi_backend.repository;

import com.example.ushi_backend.entity.LandlordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LandlordRepository extends JpaRepository<LandlordEntity, Long> {
}
