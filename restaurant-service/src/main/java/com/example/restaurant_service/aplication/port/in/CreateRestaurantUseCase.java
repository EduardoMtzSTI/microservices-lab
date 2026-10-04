package com.example.restaurant_service.aplication.port.in;

import com.example.restaurant_service.domain.model.restaurant.Restaurant;

public interface CreateRestaurantUseCase {
    Restaurant createRestaurant(CreateRestaurantCommand command);
}
