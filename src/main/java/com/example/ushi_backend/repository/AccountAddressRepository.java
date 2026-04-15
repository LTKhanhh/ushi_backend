package com.example.ushi_backend.repository;

import com.example.ushi_backend.entity.AccountAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountAddressRepository extends JpaRepository<AccountAddressEntity, Long> {
}
