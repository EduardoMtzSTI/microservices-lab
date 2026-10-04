package com.example.restaurant_service.aplication.exception;

import java.util.UUID;

public class RestaurantNotFoundException extends RuntimeException {
    public RestaurantNotFoundException(UUID id) {
        super("No se encontró el restaurant con id: "+id);
    }
}
