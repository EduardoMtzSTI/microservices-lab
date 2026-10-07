package com.example.restaurant_service.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalTime;

public record CreateRestaurantRequest(

        @NotBlank(message = "name is mandatory")
        String name,

        @NotBlank(message = "address is mandatory")
        String address,

        @NotBlank
        @Size(min = 10, max = 10, message = "phone must have 8 digits")
        String phone,

        @NotNull(message = "openingTime is mandatory")
        LocalTime openingTime,

        @NotNull(message = "closingTime is mandatory")
        LocalTime closingTime
) {
}
