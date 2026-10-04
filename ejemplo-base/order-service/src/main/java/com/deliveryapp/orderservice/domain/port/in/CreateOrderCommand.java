package com.deliveryapp.orderservice.domain.port.in;

import java.util.List;

/**
 * Representa la intención de "crear un pedido", independiente de cómo llegó
 * la petición (REST, un mensaje de Kafka, un test). El adaptador web mapea
 * su propio DTO (CreateOrderRequest) hacia este comando; nunca se pasa el
 * DTO web directamente al caso de uso.
 */
public record CreateOrderCommand(
        String customerId,
        String restaurantId,
        List<OrderItemCommand> items,
        String idempotencyKey
) {
    public record OrderItemCommand(
            String productName,
            int quantity,
            double unitPrice
    ) {}
}
