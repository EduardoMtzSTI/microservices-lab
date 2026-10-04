package com.example.customer_service.infraestructure.web;

import com.example.customer_service.aplication.port.in.CreateCustomerCommand;
import com.example.customer_service.aplication.port.in.CreateCustomerUseCase;
import com.example.customer_service.aplication.port.in.CustomerQueries;
import com.example.customer_service.domain.model.Customer;
import com.example.customer_service.infraestructure.web.dto.CreateCustomerRequest;
import com.example.customer_service.infraestructure.web.dto.CustomerResponse;
import com.example.customer_service.infraestructure.web.mapper.CustomerWebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CreateCustomerUseCase createCustomerUseCase;
    private final CustomerQueries customerQueries;
    private final CustomerWebMapper mapper;

    @PostMapping
    public ResponseEntity<CustomerResponse> create(@Valid @RequestBody CreateCustomerRequest request){

        CreateCustomerCommand command = mapper.toCommand(request);
        Customer customerCreated = createCustomerUseCase.createCustomer(command);
        CustomerResponse customerResponse = mapper.toResponse(customerCreated);

        return ResponseEntity
                .created(URI.create("/api/customers/"+customerResponse.id()))
                .body(customerResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable UUID id){
        Customer customer = customerQueries.getCustomer(id);
        CustomerResponse customerResponse = mapper.toResponse(customer);
        return ResponseEntity.ok(customerResponse);
    }

    @GetMapping()
    public ResponseEntity<List<CustomerResponse>> getAllCustomers(){
        List<Customer> customers = customerQueries.getAllCustomers();
        List<CustomerResponse> customerResponses = customers.stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(customerResponses);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.status(HttpStatus.OK).body("customer-service está funcionando correctamente");
    }

}
