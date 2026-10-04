package com.example.order_service.infraestructure.web.dto;

import com.example.order_service.domain.model.OrderStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record OrderResponse(
        UUID id,
        CustomerSummaryResponse customer,
        UUID restaurantId,
        BigDecimal totalAmount,
        String currency,
        List<ItemResponse> items,
        OrderStatus status,
        Instant createdAt,
        Instant updatedAt
) {
}
