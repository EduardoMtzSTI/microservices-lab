package com.example.payment_service.infrastructure.messaging.event;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record PaymentFailedEvent(
    UUID id,
    UUID orderId,
    UUID customerId,
    String failureReason,
    Instant createdAt
) {
}
