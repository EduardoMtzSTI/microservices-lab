package com.example.order_service.infraestructure.web.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record ItemResponse(
        UUID productId,
        String productName,
        Integer quantity,
        BigDecimal unitPrice
) {}
