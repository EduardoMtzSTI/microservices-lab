package com.example.order_service.infraestructure.messaging.event;

import java.time.Instant;
import java.util.UUID;

public record OrderAcceptedEvent(
        UUID orderId,
        UUID restaurantId,
        UUID customerId,
        Instant occurredAt
) {}
