package com.example.order_service.aplication.port.out;

import com.example.order_service.domain.model.Money;
import lombok.Builder;

import java.util.UUID;

@Builder
public record ProductPrice(
        UUID productId,
        String name,
        Money price
) {
}
