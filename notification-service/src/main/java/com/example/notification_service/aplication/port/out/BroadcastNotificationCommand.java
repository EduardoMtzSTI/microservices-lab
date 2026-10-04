package com.example.notification_service.aplication.port.out;

import lombok.Builder;

import java.time.Instant;

@Builder
public record BroadcastNotificationCommand(
        String recipientId,
        String eventType,
        String aggregateId,
        String message,
        Instant occurredAt
) {
}
