package com.example.order_service.infraestructure.messaging.event;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record OrderCreatedEvent(
    UUID orderId,
    UUID customerId,
    UUID restaurantId,
    BigDecimal totalAmount,
    String currency,
    List<OrderItemEvent> items,
    Instant occurredAt
){
    @Builder
    public record OrderItemEvent(
            String productName,
            Integer quantity,
            BigDecimal unitPrice,
            String currency
    ) {}
}
