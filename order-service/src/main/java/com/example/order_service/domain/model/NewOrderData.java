package com.example.order_service.domain.model;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record NewOrderData(
        UUID customerId,
        UUID restaurantId,
        List<OrderItem> items,
        String idempotencyKey
) {}
