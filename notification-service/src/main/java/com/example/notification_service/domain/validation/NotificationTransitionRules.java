package com.example.notification_service.domain.validation;

import com.example.notification_service.domain.model.Notification;
import com.example.notification_service.domain.model.NotificationStatus;

public interface NotificationTransitionRules {

    static Validator<Notification> isPending() {
        return notification -> notification.getStatus() == NotificationStatus.PENDING
                ? ValidationResult.SUCCESS
                : ValidationResult.invalid(
                "Solo se puede operar sobre una notificación PENDING, estado actual: " + notification.getStatus());
    }

    static Validator<Notification> hasFailureReason() {
        return notification
                -> notification.getFailureReason() == null || notification.getFailureReason().isBlank()
                ? ValidationResult.SUCCESS
                : ValidationResult.invalid(
                "failureReason es obligatorio");
    }

}