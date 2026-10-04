package com.deliveryapp.orderservice.infrastructure.web.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Lo que el cliente del API realmente ve. Nunca es la entidad de dominio
 * ni la de persistencia directamente: es su propio contrato, versionable
 * de forma independiente.
 */
public record OrderResponse(
        UUID id,
        String customerId,
        String restaurantId,
        BigDecimal totalAmount,
        String status,
        Instant createdAt,
        List<String> items
) {}
