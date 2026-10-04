package com.deliveryapp.orderservice.infrastructure.persistence.repository;

import com.deliveryapp.orderservice.infrastructure.persistence.entity.OrderJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio técnico de Spring Data. No es el puerto del dominio
 * (OrderRepositoryPort) — es un detalle de implementación que solo
 * conoce el adaptador OrderRepositoryAdapter.
 */
public interface SpringDataOrderJpaRepository extends JpaRepository<OrderJpaEntity, UUID> {
    Optional<OrderJpaEntity> findByIdempotencyKey(String idempotencyKey);
}
