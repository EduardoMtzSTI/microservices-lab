package com.deliveryapp.orderservice.infrastructure.web.exception;

import com.deliveryapp.orderservice.domain.exception.InvalidOrderException;
import com.deliveryapp.orderservice.domain.exception.OrderNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Traduce excepciones de dominio (InvalidOrderException, OrderNotFoundException)
 * y de validación del framework a respuestas HTTP consistentes. Es un detalle
 * de infraestructura web: el dominio no sabe que sus excepciones terminan
 * convertidas en un 404 o un 400.
 */
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
