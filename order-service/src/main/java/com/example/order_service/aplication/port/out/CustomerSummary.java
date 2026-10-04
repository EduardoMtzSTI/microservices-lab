package com.example.order_service.aplication.port.out;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CustomerSummary(
        UUID id,
        String fullName,
        String phone,
        String address
) {
}
