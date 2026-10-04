package com.example.order_service.infraestructure.web.exception;

import com.example.order_service.aplication.exception.OrderNotFoundException;
import com.example.order_service.domain.exception.InvalidOrderException;
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

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleOrderNotFound(OrderNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildBody(ex.getMessage()));
    }

    @ExceptionHandler(InvalidOrderException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidOrder(InvalidOrderException ex) {
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
