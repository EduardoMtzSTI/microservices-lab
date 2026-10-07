package com.example.restaurant_service.infrastructure.messaging.event;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record OrderAcceptedEvent(
        UUID orderId,
        UUID restaurantId,
        Instant occurredAt
) {}
