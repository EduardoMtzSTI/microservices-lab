package com.example.notification_service.domain.model;

import com.example.notification_service.domain.exception.InvalidNotificationException;
import com.example.notification_service.domain.validation.NotificationCreationRules;
import com.example.notification_service.domain.validation.NotificationTransitionRules;
import com.example.notification_service.domain.validation.ValidationResult;
import com.example.notification_service.domain.validation.Validator;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class Notification {

    private final UUID id;
    private final String recipientId;
    private final String eventType;
    private final String aggregateId;
    private final NotificationChannel channel;
    private final String message;
    private NotificationStatus status;
    private String failureReason;
    private final Instant createdAt;
    private Instant updatedAt;

    private Notification(NotificationSnapshot snapshot) {
        this.id = snapshot.id();
        this.recipientId = snapshot.recipientId();
        this.eventType = snapshot.eventType();
        this.aggregateId = snapshot.aggregateId();
        this.channel = snapshot.channel();
        this.message = snapshot.message();
        this.status = snapshot.status();
        this.failureReason = snapshot.failureReason();
        this.createdAt = snapshot.createdAt();
        this.updatedAt = snapshot.updatedAt();
    }

    public static Notification create(NewNotificationData data) {
        ValidationResult validation = Validator.all(
                NotificationCreationRules.hasRecipientId(),
                NotificationCreationRules.hasEventType(),
                NotificationCreationRules.hasAggregateId(),
                NotificationCreationRules.hasChannel(),
                NotificationCreationRules.hasMessage()
        ).apply(data);

        if (!validation.isValid()) {
            throw new InvalidNotificationException(String.join("; ", validation.errors()));
        }

        Instant now = Instant.now();
        return new Notification(NotificationSnapshot.builder()
                .id(UUID.randomUUID())
                .recipientId(data.recipientId())
                .eventType(data.eventType())
                .aggregateId(data.aggregateId())
                .channel(data.channel())
                .message(data.message())
                .status(NotificationStatus.PENDING)
                .createdAt(now)
                .updatedAt(now)
                .build());
    }

    public static Notification reconstitute(NotificationSnapshot snapshot) {
        return new Notification(snapshot);
    }

    public void markSent() {
        requirePending();
        this.status = NotificationStatus.SENT;
        this.updatedAt = Instant.now();
    }

    public void markFailed(String reason) {
        requirePending();
        requireFailureReason();
        this.status = NotificationStatus.FAILED;
        this.failureReason = reason;
        this.updatedAt = Instant.now();
    }

    private void requirePending() {
        ValidationResult validation = NotificationTransitionRules.isPending().apply(this);
        if (!validation.isValid()) {
            throw new InvalidNotificationException(String.join("; ", validation.errors()));
        }
    }

    private void requireFailureReason() {
        ValidationResult validation = NotificationTransitionRules.hasFailureReason().apply(this);
        if (!validation.isValid()) {
            throw new InvalidNotificationException("Se requiere un motivo para marcar la notificación como fallida");
        }
    }

}
