package com.example.restaurant_service.aplication.exception;

import java.util.UUID;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(UUID id) {
        super("No se encontró el producto con id: "+id);
    }
}
