package com.example.payment_service.domain.validation;

import com.example.payment_service.domain.model.NewPaymentData;

import java.util.Arrays;
import java.util.function.Function;

public interface PaymentValidator extends Function<NewPaymentData, ValidationResult> {

    static PaymentValidator hasOrderId() {
        return data -> data.orderId() == null
                ? ValidationResult.invalid("orderId es obligatorio")
                : ValidationResult.SUCCESS;
    }

    static PaymentValidator hasCustomerId() {
        return data -> data.customerId() == null
                ? ValidationResult.invalid("customerId es obligatorio")
                : ValidationResult.SUCCESS;
    }

    static PaymentValidator hasRestaurantId() {
        return data -> data.restaurantId() == null
                ? ValidationResult.invalid("customerId es obligatorio")
                : ValidationResult.SUCCESS;
    }

    static PaymentValidator hasAmount() {
        return data -> data.amount() == null
                ? ValidationResult.invalid("amount es obligatorio")
                : ValidationResult.SUCCESS;
    }

    static PaymentValidator hasMethod() {
        return data -> data.method() == null
                ? ValidationResult.invalid("method es obligatorio")
                : ValidationResult.SUCCESS;
    }

    default PaymentValidator and(PaymentValidator other) {
        return data -> this.apply(data).merge(other.apply(data));
    }

    static PaymentValidator all(PaymentValidator... validators) {
        return Arrays.stream(validators).reduce(data -> ValidationResult.SUCCESS, PaymentValidator::and);
    }

}
