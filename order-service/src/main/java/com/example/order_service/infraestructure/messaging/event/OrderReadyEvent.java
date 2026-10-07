package com.example.order_service.infraestructure.messaging.event;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record OrderReadyEvent(
        UUID orderId,
        UUID restaurantId,
        UUID customerId,
        Instant occurredAt
) { }
