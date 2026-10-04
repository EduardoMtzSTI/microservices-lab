package com.deliveryapp.orderservice.domain.exception;

import java.util.UUID;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(UUID orderId) {
        super("No se encontró el pedido con id: " + orderId);
    }
}
