package com.example.notification_service.infrastructure.sse;

import lombok.Builder;

import java.time.Instant;

@Builder
public record SsePayload(
        String recipientId,
        String eventType,
        String aggregateId,
        String message,
        Instant occurredAt
) {}