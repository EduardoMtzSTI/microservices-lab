package com.example.notification_service.domain.validation;

import java.util.Arrays;
import java.util.function.Function;

@FunctionalInterface
public interface Validator<T> extends Function<T, ValidationResult> {

    default Validator<T> and(Validator<T> other) {
        return data -> this.apply(data).merge(other.apply(data));
    }

    @SafeVarargs
    static <T> Validator<T> all(Validator<T>... validators) {
        return Arrays.stream(validators).reduce(data -> ValidationResult.SUCCESS, Validator::and);
    }
}
