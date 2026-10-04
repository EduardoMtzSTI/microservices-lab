package com.example.notification_service.infrastructure.sender;

import com.example.notification_service.aplication.port.out.NotificationSenderPort;
import com.example.notification_service.domain.model.Notification;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class EmailNotificationSenderAdapter implements NotificationSenderPort {

    private static final double FAILURE_RATE = 0.05;   // simula fallos ocasionales del proveedor de correo

    @Override
    public void send(Notification notification) {
        if (ThreadLocalRandom.current().nextDouble() < FAILURE_RATE) {
            throw new IllegalStateException("El proveedor de email no respondió a tiempo");
        }
        log.info("[EMAIL] a {}: {}", notification.getRecipientId(), notification.getMessage());
    }

}
