package com.example.order_service.domain.model;

import com.example.order_service.domain.exception.InvalidOrderException;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
public record OrderItem(
        UUID productId,
        String productName,
        int quantity,
        Money unitPrice
) {

    public OrderItem {
        List<String> errors = new ArrayList<>();

        if (productName == null || productName.isBlank())
            errors.add("El nombre del producto no puede estar vacío");

        if (quantity <= 0)
            errors.add("La cantidad debe ser mayor a cero, recibido: " + quantity);

        if (unitPrice == null)
            errors.add("unitPrice es obligatorio");

        if (!errors.isEmpty())
            throw new InvalidOrderException(String.join("; ", errors));
    }

    public Money subtotal() {
        return unitPrice.multiply(quantity);
    }

}
