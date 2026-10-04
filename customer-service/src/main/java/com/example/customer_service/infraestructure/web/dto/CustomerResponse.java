package com.example.customer_service.infraestructure.web.dto;


import lombok.Builder;

import java.util.UUID;

@Builder
public record CustomerResponse(
        UUID id,
        String fullName,
        String email,
        String number,
        String address
) {
}
