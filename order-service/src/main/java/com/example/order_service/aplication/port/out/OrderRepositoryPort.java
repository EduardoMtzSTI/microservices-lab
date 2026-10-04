package com.example.order_service.aplication.port.out;

import com.example.order_service.domain.model.Order;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepositoryPort {
    Order save(Order order);
    Optional<Order> findById(UUID id);
    Optional<Order> findByIdempotencyKey(String idempotencyKey);
    List<Order> findByRestaurantIdAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
            UUID restaurantId,
            Instant start,
            Instant end);
}
