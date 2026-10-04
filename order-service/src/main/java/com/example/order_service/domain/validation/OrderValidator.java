package com.example.order_service.domain.validation;

import com.example.order_service.domain.model.NewOrderData;
import java.util.Arrays;
import java.util.function.Function;

@FunctionalInterface
public interface OrderValidator extends Function<NewOrderData,ValidationResult> {

    static OrderValidator hasCustomerId(){
        return data -> (data.customerId() == null )
                ? ValidationResult.invalid("CustomerId es requerido")
                : ValidationResult.SUCCESS;
    }

    static OrderValidator hasRestaurantId(){
        return data -> (data.customerId() == null)
                ? ValidationResult.invalid("RestaurantId es requerido")
                : ValidationResult.SUCCESS;
    }

    static OrderValidator hasAtLeastOneItem() {
        return data -> (data.items() == null || data.items().isEmpty())
                ? ValidationResult.invalid("El pedido debe tener al menos un item")
                : ValidationResult.SUCCESS;
    }

    default OrderValidator and(OrderValidator other){
        return data -> this.apply(data).merge(other.apply(data));
    }

    static OrderValidator all(OrderValidator... validators){
        return Arrays.stream(validators)
                .reduce(data -> ValidationResult.SUCCESS, OrderValidator::and);
    }

}
