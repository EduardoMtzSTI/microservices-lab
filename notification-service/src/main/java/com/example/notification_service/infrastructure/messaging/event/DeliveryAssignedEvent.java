package com.example.notification_service.infrastructure.messaging.event;

import java.time.Instant;
import java.util.UUID;

public record DeliveryAssignedEvent(
        UUID orderId,
        UUID customerId,
        String courierName,
        Instant occurredAt
) {}

