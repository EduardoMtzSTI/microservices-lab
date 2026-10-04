package com.example.customer_service.aplication.exception;

import java.util.UUID;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(UUID id) {
        super("No se encontro el cliente con el id : "+id);
    }
}
