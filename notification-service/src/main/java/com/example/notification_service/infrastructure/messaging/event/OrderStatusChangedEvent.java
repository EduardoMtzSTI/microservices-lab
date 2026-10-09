package com.example.notification_service.infrastructure.messaging.event;

import java.time.Instant;
import java.util.UUID;

public record OrderStatusChangedEvent(
        UUID orderId,
        UUID customerId,
        UUID restaurantId,
        String newStatus,
        Instant occurredAt
) {}