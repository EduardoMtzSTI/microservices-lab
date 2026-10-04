package com.example.customer_service.aplication.port.out;

import com.example.customer_service.domain.model.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepositoryPort {
    Customer save(Customer customer);
    Optional<Customer> findById(UUID id);
    List<Customer> findAll();
}
