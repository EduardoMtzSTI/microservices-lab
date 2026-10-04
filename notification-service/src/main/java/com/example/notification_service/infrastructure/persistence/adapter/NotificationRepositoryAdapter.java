package com.example.notification_service.infrastructure.persistence.adapter;

import com.example.notification_service.aplication.port.out.NotificationRepositoryPort;
import com.example.notification_service.domain.model.Notification;
import com.example.notification_service.infrastructure.persistence.entity.NotificationJpaEntity;
import com.example.notification_service.infrastructure.persistence.mapper.NotificationPersistenceMapper;
import com.example.notification_service.infrastructure.persistence.repository.NotificationEntityJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class NotificationRepositoryAdapter implements NotificationRepositoryPort {

    private final NotificationEntityJpaRepository jpaRepository;
    private final NotificationPersistenceMapper mapper;

    @Override
    public Notification save(Notification notification) {
        NotificationJpaEntity notificationSaved = jpaRepository.save(mapper.toJpaEntity(notification));
        return mapper.toDomain(notificationSaved);
    }

    @Override
    public Optional<Notification> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

}
