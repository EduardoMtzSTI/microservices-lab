package com.example.restaurant_service.infraestructure.web.dto;

import java.time.LocalTime;
import java.util.UUID;

public record RestaurantResponse(
        UUID id,
        String name,
        String address,
        String phone,
        String status,
        boolean open,
        LocalTime openingTime,
        LocalTime closingTime
) {
}






