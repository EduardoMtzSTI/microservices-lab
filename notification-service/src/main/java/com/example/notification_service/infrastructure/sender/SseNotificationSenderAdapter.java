package com.example.notification_service.infrastructure.sender;

import com.example.notification_service.aplication.port.out.BroadcastNotificationCommand;
import com.example.notification_service.aplication.port.out.NotificationSenderPort;
import com.example.notification_service.aplication.port.out.SseBroadcastPort;
import com.example.notification_service.domain.model.Notification;
import com.example.notification_service.infrastructure.sse.SsePayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class SseNotificationSenderAdapter implements NotificationSenderPort {

    private final SseBroadcastPort sseBroadcastPort;

    @Override
    public void send(Notification notification) {
        BroadcastNotificationCommand command = BroadcastNotificationCommand.builder()
                .recipientId(notification.getRecipientId())
                .eventType(notification.getEventType())
                .aggregateId(notification.getAggregateId())
                .message(notification.getMessage())
                .occurredAt(notification.getCreatedAt())
                .build();
        sseBroadcastPort.broadcast(command);
    }
}
