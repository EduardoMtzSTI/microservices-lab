package com.example.notification_service.domain.validation;

import com.example.notification_service.domain.model.NewNotificationData;

public interface NotificationCreationRules {

    static Validator<NewNotificationData> hasRecipientId() {
        return data -> (data.recipientId() == null || data.recipientId().isBlank())
                ? ValidationResult.invalid("recipientId es obligatorio")
                : ValidationResult.SUCCESS;
    }

    static Validator<NewNotificationData> hasEventType() {
        return data -> (data.eventType() == null || data.eventType().isBlank())
                ? ValidationResult.invalid("eventType es obligatorio")
                : ValidationResult.SUCCESS;
    }

    static Validator<NewNotificationData> hasAggregateId() {
        return data -> (data.aggregateId() == null || data.aggregateId().isBlank())
                ? ValidationResult.invalid("aggregateId es obligatorio")
                : ValidationResult.SUCCESS;
    }

    static Validator<NewNotificationData> hasChannel() {
        return data -> data.channel() == null
                ? ValidationResult.invalid("channel es obligatorio")
                : ValidationResult.SUCCESS;
    }

    static Validator<NewNotificationData> hasMessage() {
        return data -> (data.message() == null || data.message().isBlank())
                ? ValidationResult.invalid("message es obligatorio")
                : ValidationResult.SUCCESS;
    }
}
