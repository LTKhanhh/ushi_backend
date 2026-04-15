package com.example.ushi_backend.repository;

import com.example.ushi_backend.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<CityEntity, Integer> {
}
