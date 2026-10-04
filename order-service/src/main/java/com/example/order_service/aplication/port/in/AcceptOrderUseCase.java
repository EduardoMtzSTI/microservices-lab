package com.example.order_service.aplication.port.in;

import com.example.order_service.domain.model.Order;

import java.util.UUID;

public interface AcceptOrderUseCase {
    Order accept(UUID id);
}
