package com.example.restaurant_service.domain.model.restaurant;

import lombok.Builder;

import java.time.LocalTime;

@Builder
public record NewRestaurantData(
        String name,
        String address,
        String phone,
        LocalTime openingTime,
        LocalTime closingTime
) {}