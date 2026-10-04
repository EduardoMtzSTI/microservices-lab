package com.example.order_service.infraestructure.customer;

import java.util.UUID;

public record CustomerApiResponse(
        UUID id,
        String fullName,
        String email,
        String number,
        String address
) {
}
