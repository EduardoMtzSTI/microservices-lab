package com.deliveryapp.orderservice.infrastructure.persistence.adapter;

import com.deliveryapp.orderservice.domain.model.Order;
import com.deliveryapp.orderservice.domain.port.out.OrderRepositoryPort;
import com.deliveryapp.orderservice.infrastructure.persistence.mapper.OrderPersistenceMapper;
import com.deliveryapp.orderservice.infrastructure.persistence.repository.SpringDataOrderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * Implementa OrderRepositoryPort usando Spring Data JPA. Este es el único
 * lugar de toda la aplicación donde "Order de dominio" y "OrderJpaEntity"
 * se tocan — todo lo demás (el caso de uso, el controller) solo conoce Order.
 */
@Component
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepositoryPort {

    private final SpringDataOrderJpaRepository jpaRepository;
    private final OrderPersistenceMapper mapper;

    @Override
    public Order save(Order order) {
        var savedEntity = jpaRepository.save(mapper.toJpaEntity(order));
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Order> findByIdempotencyKey(String idempotencyKey) {
        return jpaRepository.findByIdempotencyKey(idempotencyKey).map(mapper::toDomain);
    }
}
