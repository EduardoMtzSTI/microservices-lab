package com.example.restaurant_service.aplication.port.in;

import java.util.UUID;

public interface MarkOrderReadyForPickupUseCase {
    void markReadyForPickup(UUID restaurantId,UUID orderId);
}
