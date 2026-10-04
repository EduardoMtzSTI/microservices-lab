package com.example.order_service.domain.model;

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
