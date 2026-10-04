package com.example.restaurant_service.aplication.port.out;

import com.example.restaurant_service.domain.model.product.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepositoryPort{
    Product save(Product product);
    Optional<Product> findById(UUID id);
    Optional<Product> findByIdempotencyKey(String idempotencyKey);
    List<Product> findByRestaurantId(UUID restaurantId);
    List<Product> findAll();
}
