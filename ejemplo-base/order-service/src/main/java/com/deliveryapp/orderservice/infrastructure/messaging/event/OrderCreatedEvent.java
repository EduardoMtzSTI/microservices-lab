package com.deliveryapp.orderservice.infrastructure.messaging.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Contrato serializado que viaja por el tópico "order.created". Es un detalle
 * de infraestructura de mensajería — el dominio no sabe que este record existe.
 */
public record OrderCreatedEvent(
        UUID orderId,
        String customerId,
        String restaurantId,
        BigDecimal totalAmount,
        List<OrderItemEvent> items,
        Instant occurredAt
) {
    public record OrderItemEvent(
            String productName,
            Integer quantity,
            BigDecimal unitPrice
    ) {}
}
