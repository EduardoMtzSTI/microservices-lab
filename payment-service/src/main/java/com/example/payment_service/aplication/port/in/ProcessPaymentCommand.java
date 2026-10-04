package com.example.payment_service.aplication.port.in;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record ProcessPaymentCommand(
        UUID orderId,
        UUID customerId,
        UUID restaurantId,
        BigDecimal amount,
        String currency,
        String method
) {}
