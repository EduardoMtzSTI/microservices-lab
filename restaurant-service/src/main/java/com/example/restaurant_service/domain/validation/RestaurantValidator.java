package com.example.restaurant_service.domain.validation;

import com.example.restaurant_service.domain.model.restaurant.NewRestaurantData;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@FunctionalInterface
public interface RestaurantValidator extends Function<NewRestaurantData, ValidationResult> {

    Pattern NUMBER_PATTERN = Pattern.compile("^[0-9]{10}$");

    Predicate<String> isBlank= (data) -> data == null || data.isBlank();
    Predicate<String> isNumber= (data) -> NUMBER_PATTERN.matcher(data).matches();

    static RestaurantValidator hasName() {
        return data -> (isBlank.test(data.name()))
                ? ValidationResult.invalid("name es obligatorio")
                : ValidationResult.SUCCESS;
    }

    static RestaurantValidator hasAddress() {
        return data -> (isBlank.test(data.address()))
                ? ValidationResult.invalid("address es obligatorio")
                : ValidationResult.SUCCESS;
    }

    static RestaurantValidator hasPhoneValid() {
        return data -> {

                if(isBlank.test(data.phone()))
                    return ValidationResult.invalid("phone es obligatorio");

                if(!isNumber.test(data.phone()))
                    return ValidationResult.invalid("phone es debe tener 10 digitos");

                return ValidationResult.SUCCESS;
        };
    }

    static RestaurantValidator hasValidServiceHours() {
        return data -> {
            if (data.openingTime() == null || data.closingTime() == null) {
                return ValidationResult.invalid("openingTime y closingTime son obligatorios");
            }
            if (!data.openingTime().isBefore(data.closingTime())) {
                return ValidationResult.invalid("openingTime debe ser antes que closingTime");
            }
            return ValidationResult.SUCCESS;
        };
    }

    default RestaurantValidator and(RestaurantValidator other) {
        return data -> this.apply(data).merge(other.apply(data));
    }

    static RestaurantValidator all(RestaurantValidator... validators) {
        return Arrays.stream(validators)
                .reduce(data -> ValidationResult.SUCCESS, RestaurantValidator::and);
    }
}