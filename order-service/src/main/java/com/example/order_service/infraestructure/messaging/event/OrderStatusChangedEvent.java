package com.example.order_service.infraestructure.messaging.event;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record OrderStatusChangedEvent(
        UUID orderId,
        UUID restaurantId,
        UUID customerId,
        String newStatus,
        Instant occurredAt
) {
}
