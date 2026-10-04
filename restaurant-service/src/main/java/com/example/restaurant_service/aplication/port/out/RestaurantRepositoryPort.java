package com.example.restaurant_service.aplication.port.out;

import com.example.restaurant_service.domain.model.restaurant.Restaurant;

import java.util.*;

public interface RestaurantRepositoryPort {
    Restaurant save(Restaurant restaurant);
    Optional<Restaurant> findById(UUID id);
    List<Restaurant> findAll();
    List<Restaurant> findAllById(Set<UUID> restaurantIds);
}
