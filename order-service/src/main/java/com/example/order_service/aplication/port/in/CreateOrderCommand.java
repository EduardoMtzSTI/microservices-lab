package com.example.order_service.aplication.port.in;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record CreateOrderCommand(
        UUID customerId,
        UUID restaurantId,
        List<OrderItemCommand> items,
        String idempotencyKey
) {
    @Builder
    public record OrderItemCommand(
            UUID id,
            int quantity
    ) {}
}
