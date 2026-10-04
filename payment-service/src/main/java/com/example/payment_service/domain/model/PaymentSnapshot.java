package com.example.payment_service.domain.model;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record PaymentSnapshot(
        UUID id,
        UUID orderId,
        UUID customerId,
        UUID restaurantId,
        Money amount,
        PaymentMethod method,
        PaymentStatus status,
        String failureReason,
        Instant createdAt,
        Instant updatedAt
) {
}
