package com.example.ushi_backend.repository;

import com.example.ushi_backend.entity.RegistrationOtpEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegistrationOtpRepository extends JpaRepository<RegistrationOtpEntity, Long> {
    Optional<RegistrationOtpEntity> findTopByEmailOrderByCreatedAtDesc(String email);
}
