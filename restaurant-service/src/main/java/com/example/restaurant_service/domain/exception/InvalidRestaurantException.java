package com.example.restaurant_service.domain.exception;

public class InvalidRestaurantException extends RuntimeException {
    public InvalidRestaurantException(String message) {
        super(message);
    }
}
