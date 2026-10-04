package com.example.customer_service.aplication.service;

import com.example.customer_service.aplication.exception.CustomerNotFoundException;
import com.example.customer_service.aplication.port.in.CreateCustomerCommand;
import com.example.customer_service.aplication.port.in.CreateCustomerUseCase;
import com.example.customer_service.aplication.port.in.CustomerQueries;
import com.example.customer_service.aplication.port.out.CustomerRepositoryPort;
import com.example.customer_service.domain.model.Customer;
import com.example.customer_service.domain.model.NewCustomerData;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class CustomerApplicationService implements CreateCustomerUseCase, CustomerQueries {

    private final CustomerRepositoryPort customerRepository;

    @Override
    public Customer createCustomer(CreateCustomerCommand command) {

        NewCustomerData data = NewCustomerData.builder()
                .fullName(command.fullName())
                .email(command.email())
                .number(command.number())
                .address(command.address())
                .build();

        Customer customer =  Customer.create(data);
        Customer customerSaved = customerRepository.save(customer);

        return customerSaved;
    }

    @Override
    public Customer getCustomer(UUID id) {
        return customerRepository.findById(id)
                .orElseThrow(()-> new CustomerNotFoundException(id));
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
}
