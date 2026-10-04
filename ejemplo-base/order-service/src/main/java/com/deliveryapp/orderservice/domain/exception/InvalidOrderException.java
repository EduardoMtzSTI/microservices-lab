package com.deliveryapp.orderservice.domain.exception;

/**
 * Se lanza cuando los datos de un pedido violan una regla de negocio
 * (ej. lista de items vacía, cantidad inválida). No depende de ningún
 * framework: es una excepción de negocio pura.
 */
public class InvalidOrderException extends RuntimeException {
    public InvalidOrderException(String message) {
        super(message);
    }
}
