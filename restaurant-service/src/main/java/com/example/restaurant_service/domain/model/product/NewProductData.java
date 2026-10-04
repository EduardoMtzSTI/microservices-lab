package com.example.restaurant_service.domain.model.product;

import lombok.Builder;

import java.util.UUID;

@Builder
public record NewProductData(
        UUID restaurantId,
        String name,
        String description,
        Money price,
        ProductCategory category,
        String idempotencyKey
) {
}
