package com.example.restaurant_service.aplication.port.in;

import com.example.restaurant_service.domain.model.restaurant.Restaurant;

import java.util.List;
import java.util.UUID;

public interface RestaurantQueries {
    Restaurant getRestaurant(UUID id);
    List<Restaurant> listRestaurants();
}
