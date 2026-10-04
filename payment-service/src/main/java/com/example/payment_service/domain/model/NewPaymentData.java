package com.example.payment_service.domain.model;

import lombok.Builder;

import java.util.UUID;

@Builder
public record NewPaymentData(
        UUID orderId,
        UUID customerId,
        UUID restaurantId,
        Money amount,
        PaymentMethod method
) {
}
