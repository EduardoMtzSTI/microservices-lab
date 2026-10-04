package com.example.order_service.infraestructure.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.util.List;
@Builder
public record CreateOrderRequest(

        @NotBlank(message = "customerId es obligatorio")
        String customerId,

        @NotBlank(message = "restaurantId es obligatorio")
        String restaurantId,

        @NotEmpty(message = "El pedido debe tener al menos un item")
        List<@Valid OrderItemRequest> items
) {
    @Builder
    public record OrderItemRequest(

            @NotBlank(message = "id el producto es obligatorio")
            String id,

            @Positive(message = "quantity debe ser mayor a cero")
            int quantity

    ){ }
}
