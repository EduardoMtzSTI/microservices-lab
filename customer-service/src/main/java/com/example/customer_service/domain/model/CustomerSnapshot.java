package com.example.customer_service.domain.model;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record CustomerSnapshot(
        UUID id,
        String fullName,
        String email,
        String number,
        String address,
        CustomerStatus status,
        Instant createdAt,
        Instant updatedAt
) {
}
