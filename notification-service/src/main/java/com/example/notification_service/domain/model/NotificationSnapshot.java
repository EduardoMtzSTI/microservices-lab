package com.example.notification_service.domain.model;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record NotificationSnapshot(
        UUID id,
        String recipientId,
        String eventType,
        String aggregateId,
        NotificationChannel channel,
        String message,
        NotificationStatus status,
        String failureReason,
        Instant createdAt,
        Instant updatedAt
) {
}
