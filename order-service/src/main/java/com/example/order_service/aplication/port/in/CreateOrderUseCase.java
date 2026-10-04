package com.example.order_service.aplication.port.in;

import com.example.order_service.domain.model.Order;

public interface CreateOrderUseCase {
    Order createOrder(CreateOrderCommand command);
}
