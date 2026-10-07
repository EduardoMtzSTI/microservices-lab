package com.example.restaurant_service.infrastructure.web.dto;

import com.example.restaurant_service.domain.model.product.ProductCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateProductRequest(

        @NotBlank(message = "restaurantId es obligatorio")
        String restaurantId,

        @NotBlank(message = "name es obligatorio")
        String name,

        @Positive(message = "price debe ser mayor a cero")
        Double price,

        @NotBlank(message = "price es obligatorio")
        String currency,

        String description,

        @NotNull(message = "category es obligatoria")
        ProductCategory category

) { }
