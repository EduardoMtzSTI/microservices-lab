package com.deliveryapp.orderservice.domain.port.out;

import com.deliveryapp.orderservice.domain.model.Order;

import java.util.Optional;
import java.util.UUID;

/**
 * Puerto de salida (driven port): lo que el dominio NECESITA del mundo exterior
 * para persistir pedidos, sin saber si por debajo hay JPA, MongoDB o un mapa en memoria.
 * El adaptador de infraestructura (OrderRepositoryAdapter) implementa esta interfaz.
 */
public interface OrderRepositoryPort {
    Order save(Order order);
    Optional<Order> findById(UUID id);
    Optional<Order> findByIdempotencyKey(String idempotencyKey);
}
