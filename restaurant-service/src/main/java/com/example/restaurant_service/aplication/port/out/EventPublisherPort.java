package com.example.restaurant_service.aplication.port.out;

import java.util.UUID;

public interface EventPublisherPort {
    void publishRestaurantAccepted(UUID restaurantId, UUID orderId);
    void publishMarkReadyForPickup(UUID restaurantId, UUID orderId);
}
