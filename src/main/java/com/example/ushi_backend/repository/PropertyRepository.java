package com.example.ushi_backend.repository;

import com.example.ushi_backend.entity.PropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyRepository extends JpaRepository<PropertyEntity, Long> {
}
