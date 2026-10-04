package com.example.notification_service.infrastructure.persistence.entity;

import com.example.notification_service.domain.model.NotificationChannel;
import com.example.notification_service.domain.model.NotificationStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="notifications")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String recipientId;

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false)
    private String aggregateId;

    @Column(nullable = false)
    private NotificationChannel channel;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private NotificationStatus status;

    private String failureReason;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    private Instant updatedAt;
}
