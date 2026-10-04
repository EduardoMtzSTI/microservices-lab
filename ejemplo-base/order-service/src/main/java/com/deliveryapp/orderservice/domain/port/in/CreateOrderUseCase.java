package com.deliveryapp.orderservice.domain.port.in;

import com.deliveryapp.orderservice.domain.model.Order;

/**
 * Puerto de entrada (driving port): define QUÉ puede hacer el sistema,
 * sin decir CÓMO. El adaptador web (OrderController) depende de esta
 * interfaz, no de la implementación concreta — así el controller nunca
 * sabe si el caso de uso está implementado con JPA, Kafka, o ambos.
 */
public interface CreateOrderUseCase {
    Order createOrder(CreateOrderCommand command);
}
