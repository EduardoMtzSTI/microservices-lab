package com.example.customer_service.aplication.port.in;

import com.example.customer_service.domain.model.Customer;

public interface CreateCustomerUseCase {
    Customer createCustomer(CreateCustomerCommand command);
}
