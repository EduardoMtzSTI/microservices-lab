package com.deliveryapp.orderservice.domain.port.in;

import com.deliveryapp.orderservice.domain.model.Order;

import java.util.UUID;

public interface GetOrderUseCase {
    Order getOrder(UUID orderId);
}
