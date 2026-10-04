package com.example.notification_service.aplication.port.out;

public interface SseBroadcastPort {
    void broadcast(BroadcastNotificationCommand command);
}
