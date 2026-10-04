package com.example.order_service.infraestructure.catalog;

import java.math.BigDecimal;
import java.util.UUID;

public record RestaurantProductApiResponse(
        UUID id,
        String name,
        BigDecimal price,
        String currency
) { }
