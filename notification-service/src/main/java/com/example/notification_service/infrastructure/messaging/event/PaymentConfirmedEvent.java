package com.example.notification_service.infrastructure.messaging.event;

import java.time.Instant;
import java.util.UUID;

public record PaymentConfirmedEvent(
        UUID paymentId,
        UUID orderId,
        UUID customerId,
        UUID restaurantId,
        Instant occurredAt
) {}

