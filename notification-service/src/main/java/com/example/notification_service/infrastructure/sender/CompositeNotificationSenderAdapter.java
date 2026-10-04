package com.example.notification_service.infrastructure.sender;

import com.example.notification_service.aplication.port.out.NotificationSenderPort;
import com.example.notification_service.domain.model.Notification;
import com.example.notification_service.domain.model.NotificationChannel;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class CompositeNotificationSenderAdapter implements NotificationSenderPort {

    private final Map<NotificationChannel, NotificationSenderPort> sendersByChannel;

    @Override
    public void send(Notification notification) {
        NotificationSenderPort sender = sendersByChannel.get(notification.getChannel());
        if (sender == null) {
            throw new IllegalStateException("No hay un sender configurado para el canal " + notification.getChannel());
        }
        sender.send(notification);
    }

}
