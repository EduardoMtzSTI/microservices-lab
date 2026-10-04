package com.example.restaurant_service.aplication.port.in;

import com.example.restaurant_service.domain.model.product.ProductCategory;
import lombok.Builder;

import java.util.UUID;

@Builder
public record CreateProductCommand(
      UUID restaurantId,
      String name,
      Double price,
      String currency,
      String description,
      ProductCategory category,
      String idempotencyKey
) {
}
