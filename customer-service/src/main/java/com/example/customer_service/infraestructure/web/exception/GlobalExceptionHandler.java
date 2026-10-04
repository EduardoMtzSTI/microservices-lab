package com.example.customer_service.infraestructure.web.exception;

import com.example.customer_service.aplication.exception.CustomerNotFoundException;
import com.example.customer_service.domain.exception.InvalidCustomerException;
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

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleOrderNotFound(CustomerNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildBody(ex.getMessage()));
    }

    @ExceptionHandler(InvalidCustomerException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidOrder(InvalidCustomerException ex) {
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
