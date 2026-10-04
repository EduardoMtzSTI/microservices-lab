package com.example.notification_service.infrastructure.messaging.event;

import java.time.Instant;
import java.util.UUID;

public record RestaurantAcceptedEvent(
        UUID orderId,
        UUID restaurantId,
        UUID customerId,
        Instant occurredAt
) {}
