package com.example.order_service.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

public record Money(
    long amount,
    Currency currency
) {

    public Money{
        Objects.requireNonNull(currency,"La moneda es obligatoria");
        if (amount < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo: " + amount);
        }
    }

    public static Money zero(Currency currency){
        return new Money(0L, currency);
    }

    public Money plus(Money other) {
        requireSameCurrency(other);
        return new Money(this.amount + other.amount, this.currency);
    }

    public Money multiply(int quantity) {
        return new Money(this.amount * quantity, this.currency);
    }

    private void requireSameCurrency(Money other){
        if(!this.currency.equals(other.currency)){
            throw new IllegalArgumentException(
                    "No se pueden combinar montos de monedas distintas: %s vs %s"
                    .formatted(this.currency.getCurrencyCode(),other.currency.getCurrencyCode())
            );
        }
    }

    public BigDecimal toDecimal(){
        int fractionDigits = currency.getDefaultFractionDigits();
        return BigDecimal.valueOf(amount,fractionDigits);
    }

    public static Money of(double majorUnitsAmount, Currency currency) {
        long minorUnits = BigDecimal.valueOf(majorUnitsAmount)
                .movePointRight(currency.getDefaultFractionDigits())
                .setScale(0, RoundingMode.HALF_UP)
                .longValueExact();
        return new Money(minorUnits, currency);
    }

    public static Money of(BigDecimal majorUnitsAmount, Currency currency) {
        long minorUnits = majorUnitsAmount
                .movePointRight(currency.getDefaultFractionDigits())
                .setScale(0, RoundingMode.HALF_UP)
                .longValueExact();
        return new Money(minorUnits, currency);
    }

}
