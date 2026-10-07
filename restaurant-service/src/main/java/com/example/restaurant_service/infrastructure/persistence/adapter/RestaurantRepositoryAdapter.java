package com.example.restaurant_service.infrastructure.persistence.adapter;

import com.example.restaurant_service.aplication.port.out.RestaurantRepositoryPort;
import com.example.restaurant_service.domain.model.restaurant.Restaurant;
import com.example.restaurant_service.infrastructure.persistence.mapper.RestaurantPersistenceMapper;
import com.example.restaurant_service.infrastructure.persistence.repository.SpringDataRestaurantJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RestaurantRepositoryAdapter implements RestaurantRepositoryPort {

    private final SpringDataRestaurantJpaRepository jpaRepository;
    private final RestaurantPersistenceMapper mapper;

    @Override
    public Restaurant save(Restaurant restaurant) {
        var savedEntity = jpaRepository.save(mapper.toJpaEntity(restaurant));
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Restaurant> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Restaurant> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Restaurant> findAllById(Set<UUID> restaurantIds) {
        return jpaRepository.findAllById(restaurantIds).stream()
                .map(mapper::toDomain)
                .toList();
    }

}