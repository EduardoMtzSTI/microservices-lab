package com.example.customer_service.aplication.port.in;

import com.example.customer_service.domain.model.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomerQueries {
    Customer getCustomer(UUID id);
    List<Customer> getAllCustomers();
}
