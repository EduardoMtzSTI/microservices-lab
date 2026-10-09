package com.example.order_service.aplication.port.out;

import com.example.order_service.domain.model.Order;

public interface OrderEventPublisherPort {
    void publishOrderCreated(Order order);
    void publishOrderStatusChange(Order order);
}

