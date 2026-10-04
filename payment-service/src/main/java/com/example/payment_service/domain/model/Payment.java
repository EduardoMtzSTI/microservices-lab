package com.example.payment_service.domain.model;

import com.example.payment_service.domain.exception.InvalidPaymentException;
import com.example.payment_service.domain.validation.PaymentValidator;
import com.example.payment_service.domain.validation.ValidationResult;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class Payment {

    private final UUID id;
    private final UUID orderId;
    private final UUID customerId;
    private final UUID restaurantId;
    private final Money amount;
    private final PaymentMethod method;
    private PaymentStatus status;
    private String failureReason;
    private final Instant createdAt;
    private Instant updatedAt;

    private Payment(PaymentSnapshot snapshot) {
        this.id = snapshot.id();
        this.orderId = snapshot.orderId();
        this.customerId = snapshot.customerId();
        this.restaurantId = snapshot.restaurantId();
        this.amount = snapshot.amount();
        this.method = snapshot.method();
        this.status = snapshot.status();
        this.failureReason = snapshot.failureReason();
        this.createdAt = snapshot.createdAt();
        this.updatedAt = snapshot.updatedAt();
    }

    public static Payment create(NewPaymentData data) {
        ValidationResult validation = PaymentValidator.all(
                PaymentValidator.hasOrderId(),
                PaymentValidator.hasCustomerId(),
                PaymentValidator.hasRestaurantId(),
                PaymentValidator.hasAmount(),
                PaymentValidator.hasMethod()
        ).apply(data);

        if (!validation.isValid()) {
            throw new InvalidPaymentException(String.join("; ", validation.errors()));
        }

        Instant now = Instant.now();
        return new Payment(PaymentSnapshot.builder()
                .id(UUID.randomUUID())
                .orderId(data.orderId())
                .customerId(data.customerId())
                .restaurantId(data.restaurantId())
                .amount(data.amount())
                .method(data.method())
                .status(PaymentStatus.PENDING)
                .createdAt(now)
                .updatedAt(now)
                .build());
    }

    public static Payment reconstitute(PaymentSnapshot snapshot) {
        return new Payment(snapshot);
    }

    public void confirm() {
        requirePending("confirmar");
        this.status = PaymentStatus.CONFIRMED;
        this.updatedAt = Instant.now();
    }

    public void fail(String reason) {
        requirePending("marcar como fallido");
        if (reason == null || reason.isBlank()) {
            throw new InvalidPaymentException("Se requiere un motivo para marcar el pago como fallido");
        }
        this.status = PaymentStatus.FAILED;
        this.failureReason = reason;
        this.updatedAt = Instant.now();
    }

    private void requirePending(String action) {
        if (this.status != PaymentStatus.PENDING) {
            throw new InvalidPaymentException(
                    "Solo se puede %s un pago en estado PENDING, estado actual: %s".formatted(action, this.status));
        }
    }

}
