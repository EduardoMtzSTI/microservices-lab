package com.example.notification_service.aplication.port.out;

import com.example.notification_service.domain.model.Notification;

public interface NotificationSenderPort {
    void send(Notification notification);
}
