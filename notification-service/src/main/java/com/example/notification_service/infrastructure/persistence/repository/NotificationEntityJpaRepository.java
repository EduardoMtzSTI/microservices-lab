package com.example.notification_service.infrastructure.persistence.repository;

import com.example.notification_service.infrastructure.persistence.entity.NotificationJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationEntityJpaRepository extends JpaRepository<NotificationJpaEntity, UUID> {
}
