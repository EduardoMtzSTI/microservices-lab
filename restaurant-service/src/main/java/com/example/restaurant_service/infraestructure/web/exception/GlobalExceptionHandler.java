package com.example.restaurant_service.infraestructure.web.exception;

import com.example.restaurant_service.aplication.exception.ProductNotFoundException;
import com.example.restaurant_service.aplication.exception.RestaurantNotFoundException;
import com.example.restaurant_service.domain.exception.InvalidProductException;
import com.example.restaurant_service.domain.exception.InvalidRestaurantException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            ProductNotFoundException.class,
            RestaurantNotFoundException.class
    })
    public ResponseEntity<Map<String, Object>> handleNotFound(ProductNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildBody(ex.getMessage()));
    }

    @ExceptionHandler({
            InvalidProductException.class,
            InvalidRestaurantException.class
    })
    public ResponseEntity<Map<String, Object>> handleInvalid(InvalidProductException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildBody(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("Datos inválidos");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildBody(message));
    }

    private Map<String, Object> buildBody(String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now());
        body.put("message", message);
        return body;
    }
}
