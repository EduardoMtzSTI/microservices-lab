package com.example.restaurant_service.domain.model.restaurant;

import lombok.Builder;

import java.time.Instant;
import java.time.LocalTime;
import java.util.UUID;

@Builder
public record RestaurantSnapshot(
        UUID id,
        String name,
        String address,
        String phone,
        RestaurantStatus status,
        boolean open,
        LocalTime openingTime,
        LocalTime closingTime,
        Instant createdAt,
        Instant updatedAt
) {}