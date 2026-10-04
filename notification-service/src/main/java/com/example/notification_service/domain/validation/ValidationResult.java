package com.example.notification_service.domain.validation;

import java.util.ArrayList;
import java.util.List;

public record ValidationResult(List<String> errors) {

    public static final ValidationResult SUCCESS = new ValidationResult(List.of());

    public boolean isValid() {
        return errors.isEmpty();
    }

    public ValidationResult merge(ValidationResult other) {
        if (this.isValid() && other.isValid()) {
            return SUCCESS;
        }
        List<String> combined = new ArrayList<>(this.errors);
        combined.addAll(other.errors);
        return new ValidationResult(combined);
    }

    public static ValidationResult invalid(String message) {
        return new ValidationResult(List.of(message));
    }
}
