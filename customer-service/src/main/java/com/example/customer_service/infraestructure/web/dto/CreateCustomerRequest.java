package com.example.customer_service.infraestructure.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCustomerRequest(

        @NotBlank
        String fullName,

        @Email
        String email,

        @NotBlank
        @Size(min = 10, max = 10, message = "El número debe tener exactamente 8 caracteres")
        String number,

        @NotBlank
        String address
) {
}
