package com.example.restaurant_service.domain.model.product;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record ProductSnapshot(
        UUID id,
        UUID restaurantId,
        String name,
        Money price,
        String description,
        ProductCategory category,
        boolean available,
        Instant createdAt,
        Instant updatedAt,
        String idempotencyKey
) {
}