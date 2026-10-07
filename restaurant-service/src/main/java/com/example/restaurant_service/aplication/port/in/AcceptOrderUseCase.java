package com.example.restaurant_service.aplication.port.in;

import java.util.UUID;

public interface AcceptOrderUseCase {
    void accept(UUID restaurantId,UUID orderId);
}
