package com.example.order_service.infraestructure.messaging.event;

import java.time.Instant;
import java.util.UUID;

public record PaymentConfirmedEvent(
        UUID paymentId,
        UUID orderId,
        UUID customerId,
        Instant occurredAt
) {}

