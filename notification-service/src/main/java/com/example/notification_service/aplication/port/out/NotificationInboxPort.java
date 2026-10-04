package com.example.notification_service.aplication.port.out;

public interface NotificationInboxPort {
    boolean tryMarkAsProcessed(String eventType, String aggregateId, String recipientId);
}
