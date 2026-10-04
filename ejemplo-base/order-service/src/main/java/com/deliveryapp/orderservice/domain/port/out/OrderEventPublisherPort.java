package com.deliveryapp.orderservice.domain.port.out;

import com.deliveryapp.orderservice.domain.model.Order;

/**
 * Puerto de salida para notificar al resto del sistema que ocurrió algo
 * relevante en el dominio. El dominio solo sabe que "hay que avisar que
 * se creó un pedido" — no sabe que eso se traduce en Kafka + Outbox pattern.
 * Esa decisión vive enteramente en el adaptador de infraestructura.
 */
public interface OrderEventPublisherPort {
    void publishOrderCreated(Order order);
}
