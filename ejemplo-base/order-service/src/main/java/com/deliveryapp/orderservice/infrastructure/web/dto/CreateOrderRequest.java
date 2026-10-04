package com.deliveryapp.orderservice.infrastructure.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Cuerpo esperado en POST /api/orders. Vive en infraestructura.web porque es
 * un detalle de transporte HTTP, no un concepto de negocio — por eso el
 * caso de uso nunca recibe esta clase directamente, sino un CreateOrderCommand.
 */
@Getter
@Setter
public class CreateOrderRequest {

    @NotBlank(message = "customerId es obligatorio")
    private String customerId;

    @NotBlank(message = "restaurantId es obligatorio")
    private String restaurantId;

    @NotEmpty(message = "El pedido debe tener al menos un item")
    private List<@Valid OrderItemRequest> items;

    @Getter
    @Setter
    public static class OrderItemRequest {

        @NotBlank(message = "productName es obligatorio")
        private String productName;

        @Positive(message = "quantity debe ser mayor a cero")
        private int quantity;

        @PositiveOrZero(message = "unitPrice no puede ser negativo")
        private double unitPrice;
    }
}
