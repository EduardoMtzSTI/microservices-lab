package com.example.customer_service.infraestructure.persistence.repository;

import com.example.customer_service.infraestructure.persistence.entity.CustomerJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataCustomerJpaRepository extends JpaRepository<CustomerJpaEntity, UUID> {
}
