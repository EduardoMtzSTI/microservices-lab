package com.example.restaurant_service.domain.validation;

import com.example.restaurant_service.domain.model.product.NewProductData;

import java.util.Arrays;
import java.util.function.Function;

@FunctionalInterface
public interface ProductValidator extends Function<NewProductData, ValidationResult> {

    long MIN_PRICE = 0;
    long MAX_PRICE = 10_000;

    static ProductValidator hasName(){
        return data -> (data.name() == null || data.name().isBlank())
                ? ValidationResult.invalid("Nombre es obligatorio;")
                : ValidationResult.SUCCESS;
    }

    static ProductValidator hasRestaurantId(){
        return data -> (data.restaurantId() == null)
                ? ValidationResult.invalid("restaurantId es obligatorio;")
                : ValidationResult.SUCCESS;
    }

    static ProductValidator hasCategory() {
        return data -> data.category() == null
                ? ValidationResult.invalid("category es obligatorio;")
                : ValidationResult.SUCCESS;
    }

    static ProductValidator isPriceValid(){
        return data ->
                (data.price() == null || data.price().amount() > MIN_PRICE && MAX_PRICE < data.price().amount())
                ? ValidationResult.invalid("El precio debe ser mayor a %s y menor a %s;".formatted(MIN_PRICE,MAX_PRICE))
                : ValidationResult.SUCCESS;
    }

    default ProductValidator and(ProductValidator other){
        return data -> this.apply(data).merge(other.apply(data));
    }

    static ProductValidator all(ProductValidator... validators){
        return Arrays.stream(validators)
                .reduce(data -> ValidationResult.SUCCESS, ProductValidator::and);
    }

}
