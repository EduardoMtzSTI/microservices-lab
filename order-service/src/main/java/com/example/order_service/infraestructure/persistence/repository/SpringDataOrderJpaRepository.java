package com.example.order_service.infraestructure.persistence.repository;

import com.example.order_service.infraestructure.persistence.entity.OrderJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataOrderJpaRepository extends JpaRepository<OrderJpaEntity, UUID> {
    Optional<OrderJpaEntity> findByIdempotencyKey(String idempotencyKey);
    List<OrderJpaEntity> findByRestaurantIdAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
            UUID restaurantId,
            Instant start,
            Instant end
    );
}
