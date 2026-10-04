package com.deliveryapp.orderservice.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Value Object para representar montos de dinero.
 *
 * Por qué no usar Double/double para dinero: los tipos de punto flotante binario
 * (float/double) no pueden representar exactamente la mayoría de los valores
 * decimales (ej. 0.1 + 0.2 != 0.3 en binario), lo que produce errores de
 * redondeo acumulados en cálculos financieros. BigDecimal es la forma correcta
 * de modelar dinero en Java.
 *
 * Al ser un "record" es inmutable por diseño: cualquier operación (plus, multiply)
 * devuelve una nueva instancia en vez de mutar la existente.
 */
public record Money(BigDecimal amount) {

    public Money {
        Objects.requireNonNull(amount, "El monto no puede ser nulo");
        if (amount.signum() < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo: " + amount);
        }
        // Normalizamos siempre a 2 decimales para evitar inconsistencias de escala
        amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    public static Money zero() {
        return new Money(BigDecimal.ZERO);
    }

    public static Money of(double value) {
        return new Money(BigDecimal.valueOf(value));
    }

    public Money plus(Money other) {
        return new Money(this.amount.add(other.amount));
    }

    public Money multiply(int quantity) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(quantity)));
    }
}
