package com.example.notification_service.infrastructure.sender;

import com.example.notification_service.aplication.port.out.NotificationSenderPort;
import com.example.notification_service.domain.model.Notification;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PushNotificationSenderAdapter implements NotificationSenderPort {

    @Override
    public void send(Notification notification) {
        log.info("[PUSH] a {}: {}", notification.getRecipientId(), notification.getMessage());
    }

}