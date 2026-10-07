package com.example.customer_service.domain.model;

import com.example.customer_service.domain.exception.InvalidCustomerException;
import com.example.customer_service.domain.validation.CustomerValidator;
import com.example.customer_service.domain.validation.ValidationResult;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class Customer {

    private final UUID id;
    private final String fullName;
    private String email;
    private String number;
    private String address;
    private CustomerStatus status;
    private final Instant createdAt;
    private Instant updatedAt;

    private Customer(CustomerSnapshot data){
        this.id = data.id();
        this.fullName = data.fullName();
        this.email = data.email();
        this.number = data.number();
        this.address = data.address();
        this.status = data.status();
        this.createdAt = data.createdAt();
        this.updatedAt = data.updatedAt();
    }

    public static Customer create(NewCustomerData data){

        ValidationResult validator = CustomerValidator.all(
                CustomerValidator.hasFullname(),
                CustomerValidator.isEmailValid(),
                CustomerValidator.hasAddress(),
                CustomerValidator.hasNumber()
        ).apply(data);

        if(!validator.isValid())
            throw new InvalidCustomerException(String.join(";", validator.errors()));

        Instant now = Instant.now();

        CustomerSnapshot customerSnapshot = CustomerSnapshot.builder()
                .id(UUID.randomUUID())
                .fullName(data.fullName())
                .email(data.email())
                .number(data.number())
                .address(data.address())
                .status(CustomerStatus.ACTIVE)
                .createdAt(now)
                .updatedAt(now)
                .build();

        return new Customer(customerSnapshot);
    }

    public static Customer reconstitute(CustomerSnapshot data){
        return new Customer(data);
    }
}
