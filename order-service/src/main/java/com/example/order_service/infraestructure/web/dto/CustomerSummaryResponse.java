package com.example.order_service.infraestructure.web.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CustomerSummaryResponse(
        UUID id,
        String phone,
        String fullName,
        String address
) {
}
