package com.example.notification_service.domain.model;

import lombok.Builder;

@Builder
public record NewNotificationData(
        String recipientId,
        String eventType,
        String aggregateId,
        NotificationChannel channel,
        String message
) { }
