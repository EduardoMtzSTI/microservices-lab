package com.example.restaurant_service.aplication.query;

import com.example.restaurant_service.domain.model.product.Product;
import com.example.restaurant_service.domain.model.restaurant.Restaurant;

public record ProductWithRestaurantView(
        Product product,
        Restaurant restaurant
) {}