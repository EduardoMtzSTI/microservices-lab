package com.example.restaurant_service.infrastructure.persistence.adapter;

import com.example.restaurant_service.aplication.port.out.ProductRepositoryPort;
import com.example.restaurant_service.domain.model.product.Product;
import com.example.restaurant_service.infrastructure.persistence.entity.ProductJpaEntity;
import com.example.restaurant_service.infrastructure.persistence.mapper.ProductPersistenceMapper;
import com.example.restaurant_service.infrastructure.persistence.repository.SpringDataProductJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepositoryPort {

    private final SpringDataProductJpaRepository jpaRepository;
    private final ProductPersistenceMapper mapper;

    @Override
    public Product save(Product product) {
        ProductJpaEntity productSaved = jpaRepository.save(mapper.toJpaEntity(product));
        return mapper.toDomain(productSaved);
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Product> findByIdempotencyKey(String idempotencyKey) {
        return jpaRepository.findByIdempotencyKey(idempotencyKey)
                .map(mapper::toDomain);
    }

    @Override
    public List<Product> findByRestaurantId(UUID restaurantId) {
        return jpaRepository.findByRestaurantId(restaurantId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Product> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

}
