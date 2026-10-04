package com.example.notification_service.aplication.port.in;

import com.example.notification_service.domain.model.NotificationChannel;
import lombok.Builder;

@Builder
public record SendNotificationCommand(
        String recipientId,
        String eventType,
        String aggregateId,
        NotificationChannel channel,
        String message
) {
}
