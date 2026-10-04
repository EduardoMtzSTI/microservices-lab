package com.example.notification_service.infrastructure.messaging.event;

import java.time.Instant;
import java.util.UUID;

public record PaymentFailedEvent(
        UUID paymentId,
        UUID orderId,
        UUID customerId,
        String reason,
        Instant occurredAt
) {}
