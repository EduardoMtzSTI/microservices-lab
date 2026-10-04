package com.example.notification_service.aplication.port.out;

import com.example.notification_service.domain.model.Notification;

import java.util.Optional;
import java.util.UUID;

public interface NotificationRepositoryPort {
    Notification save(Notification notification);
    Optional<Notification> findById(UUID id);
}
