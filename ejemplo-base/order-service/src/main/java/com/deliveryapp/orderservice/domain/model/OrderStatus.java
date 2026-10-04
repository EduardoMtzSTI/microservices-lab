package com.deliveryapp.orderservice.domain.model;

/**
 * Ciclo de vida del pedido dentro de la saga de delivery.
 * Vive en el dominio porque es una regla de negocio, no un detalle técnico.
 */
public enum OrderStatus {
    CREATED,
    PAYMENT_CONFIRMED,
    PAYMENT_FAILED,
    RESTAURANT_ACCEPTED,
    READY_FOR_PICKUP,
    DELIVERY_ASSIGNED,
    ON_THE_WAY,
    DELIVERED,
    CANCELLED
}
