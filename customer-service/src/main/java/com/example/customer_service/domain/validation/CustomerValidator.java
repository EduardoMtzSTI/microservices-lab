package com.example.customer_service.domain.validation;

import com.example.customer_service.domain.model.NewCustomerData;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@FunctionalInterface
public interface CustomerValidator extends Function<NewCustomerData,ValidationResult> {

    Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    Pattern NUMBER_PATTERN = Pattern.compile("^[0-9]{10}$");

    Predicate<String> isBlank= (data) -> data == null || data.isBlank();
    Predicate<String> isEmail= (data) -> !isBlank.test(data) || EMAIL_PATTERN.matcher(data).matches();
    Predicate<String> isNumber= (data) -> !isBlank.test(data) || NUMBER_PATTERN.matcher(data).matches();

    static CustomerValidator hasFullname(){
        return data -> (isBlank.test(data.fullName()))
                ? ValidationResult.invalid("Fullname es obligatorio;")
                : ValidationResult.SUCCESS;
    }

    static CustomerValidator isEmailValid(){
        return data -> (!isEmail.test(data.email()))
                ? ValidationResult.invalid("Email no es valido;")
                : ValidationResult.SUCCESS;
    }

    static CustomerValidator hasNumber(){
        return data -> (!isNumber.test(data.number()))
                ? ValidationResult.invalid("Number es invalido, debe contener 8 digitos;")
                : ValidationResult.SUCCESS;
    }

    static CustomerValidator hasAddress(){
        return data -> (isBlank.test(data.address()))
                ? ValidationResult.invalid("Address es obligatorio;")
                : ValidationResult.SUCCESS;
    }

    default CustomerValidator and(CustomerValidator other){
        return data -> this.apply(data).merge(other.apply(data));
    }

    static CustomerValidator all(CustomerValidator... validators){
        return Arrays.stream(validators)
                .reduce(data -> ValidationResult.SUCCESS, CustomerValidator::and);
    }

}
