package com.example.restaurant_service.infraestructure.web.dto;

import com.example.restaurant_service.domain.model.product.ProductCategory;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalTime;
import java.util.UUID;

@Builder
public record ProductResponse(
        UUID id,
        String name,
        BigDecimal price,
        String currency,
        String description,
        ProductCategory category,
        Boolean available,
        RestaurantSummary restaurant
) {
    @Builder
    public record RestaurantSummary(
            UUID id,
            String name,
            LocalTime openingTime,
            LocalTime closingTime,
            boolean open
    ) {}
}
