package com.example.restaurant_service.infraestructure.persistence.repository;

import com.example.restaurant_service.infraestructure.persistence.entity.ProductJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataProductJpaRepository extends JpaRepository<ProductJpaEntity, UUID> {
    Optional<ProductJpaEntity> findByIdempotencyKey(String idempotencyKey);
    List<ProductJpaEntity> findByRestaurantId(UUID restaurantId);
}
