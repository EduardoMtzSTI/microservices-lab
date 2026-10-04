package com.example.customer_service.domain.model;

import lombok.Builder;

@Builder
public record NewCustomerData(
        String fullName,
        String email,
        String number,
        String address
) {
}
