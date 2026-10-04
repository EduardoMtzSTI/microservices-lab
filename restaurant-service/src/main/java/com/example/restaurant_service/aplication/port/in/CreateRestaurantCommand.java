package com.example.restaurant_service.aplication.port.in;

import lombok.Builder;

import java.time.LocalTime;

@Builder
public record CreateRestaurantCommand(
        String name,
        String address,
        String phone,
        LocalTime openingTime,
        LocalTime closingTime
) {
}
