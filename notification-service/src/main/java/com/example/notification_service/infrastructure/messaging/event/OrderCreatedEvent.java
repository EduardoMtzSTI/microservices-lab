package com.example.notification_service.infrastructure.messaging.event;

import java.time.Instant;
import java.util.UUID;

public record OrderCreatedEvent(
        UUID orderId,
        UUID customerId,
        UUID restaurantId,
        Instant occurredAt
) { }
