package com.example.customer_service.infraestructure.web.mapper;

import com.example.customer_service.aplication.port.in.CreateCustomerCommand;
import com.example.customer_service.domain.model.Customer;
import com.example.customer_service.infraestructure.web.dto.CreateCustomerRequest;
import com.example.customer_service.infraestructure.web.dto.CustomerResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerWebMapper {

    CustomerResponse toResponse(Customer customer);

    CreateCustomerCommand toCommand(CreateCustomerRequest request);
}
