package com.example.order_service.infraestructure.persistence.adapter;

import com.example.order_service.aplication.port.out.OrderRepositoryPort;
import com.example.order_service.domain.model.Order;
import com.example.order_service.infraestructure.persistence.mapper.OrderPersistenceMapper;
import com.example.order_service.infraestructure.persistence.repository.SpringDataOrderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    @Override
    public List<Order> findByRestaurantIdAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
            UUID restaurantId,
            Instant start,
            Instant end) {
        return jpaRepository.findByRestaurantIdAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
                    restaurantId,
                    start,
                    end)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}
