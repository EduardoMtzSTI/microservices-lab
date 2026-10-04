package com.example.customer_service.infraestructure.persistence.adapter;

import com.example.customer_service.aplication.port.out.CustomerRepositoryPort;
import com.example.customer_service.domain.model.Customer;
import com.example.customer_service.infraestructure.persistence.entity.CustomerJpaEntity;
import com.example.customer_service.infraestructure.persistence.mapper.CustomerPersistenceMapper;
import com.example.customer_service.infraestructure.persistence.repository.SpringDataCustomerJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomerRepositoryAdapter implements CustomerRepositoryPort {

    private final SpringDataCustomerJpaRepository repository;
    private final CustomerPersistenceMapper mapper;

    @Override
    public Customer save(Customer customer) {
        CustomerJpaEntity customerJpaEntity = repository.save(mapper.toJpaEntity(customer));
        return mapper.toDomain(customerJpaEntity);
    }

    @Override
    public Optional<Customer> findById(UUID id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Customer> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }
}
