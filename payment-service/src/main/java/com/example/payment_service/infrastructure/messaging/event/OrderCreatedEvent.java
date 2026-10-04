package com.example.payment_service.infrastructure.messaging.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderCreatedEvent(
        UUID orderId,
        UUID customerId,
        UUID restaurantId,
        BigDecimal totalAmount,
        String currency,
        List<OrderItemEvent> items,
        Instant occurredAt
){
    public record OrderItemEvent(
            String productName,
            Integer quantity,
            BigDecimal unitPrice,
            String currency
    ) {}
}

