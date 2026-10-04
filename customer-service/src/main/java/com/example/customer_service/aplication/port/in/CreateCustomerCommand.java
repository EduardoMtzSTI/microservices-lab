package com.example.customer_service.aplication.port.in;

import lombok.Builder;

@Builder
public record CreateCustomerCommand(
        String fullName,
        String email,
        String number,
        String address
) {
}
