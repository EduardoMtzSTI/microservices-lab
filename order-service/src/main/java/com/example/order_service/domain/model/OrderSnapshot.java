package com.example.order_service.domain.model;

import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record OrderSnapshot(
        UUID id,
        UUID customerId,
        UUID restaurantId,
        Money totalAmount,
        List<OrderItem>items,
        OrderStatus status,
        Instant createdAt,
        Instant updatedAt,
        String idempotencyKey
) {
}
