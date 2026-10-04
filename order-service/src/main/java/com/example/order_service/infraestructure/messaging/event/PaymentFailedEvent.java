package com.example.order_service.infraestructure.messaging.event;

import java.time.Instant;
import java.util.UUID;

public record PaymentFailedEvent(
    UUID id,
    UUID orderId,
    UUID customerId,
    String failureReason,
    Instant createdAt
) {
}
