package com.example.restaurant_service.infrastructure.messaging.event;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record OrderReadyEvent(
        UUID orderId,
        UUID restaurantId,
        Instant occurredAt
) { }
