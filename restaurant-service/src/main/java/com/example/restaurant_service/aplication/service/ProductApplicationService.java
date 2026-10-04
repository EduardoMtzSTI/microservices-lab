package com.example.restaurant_service.aplication.service;

import com.example.restaurant_service.aplication.exception.ProductNotFoundException;
import com.example.restaurant_service.aplication.port.in.CreateProductCommand;
import com.example.restaurant_service.aplication.port.in.CreateProductUseCase;
import com.example.restaurant_service.aplication.port.in.ProductQueries;
import com.example.restaurant_service.aplication.port.out.ProductRepositoryPort;
import com.example.restaurant_service.aplication.port.out.RestaurantRepositoryPort;
import com.example.restaurant_service.aplication.query.ProductWithRestaurantView;
import com.example.restaurant_service.domain.model.product.Money;
import com.example.restaurant_service.domain.model.product.NewProductData;
import com.example.restaurant_service.domain.model.product.Product;
import com.example.restaurant_service.domain.model.restaurant.Restaurant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class ProductApplicationService implements CreateProductUseCase, ProductQueries {

    private final ProductRepositoryPort productRepository;
    private final RestaurantRepositoryPort restaurantRepository;

    @Override
    public Product createProduct(CreateProductCommand command) {

        if (command.idempotencyKey() != null && !command.idempotencyKey().isBlank()) {
            Optional<Product> existingProduct = productRepository.findByIdempotencyKey(command.idempotencyKey());
            if (existingProduct.isPresent()) {
                log.info("Idempotency-Key {} ya procesada, devolviendo el producto existente {}",
                        command.idempotencyKey(), existingProduct.get().getId());
                return existingProduct.get();
            }
        }

        NewProductData data = NewProductData.builder()
                .restaurantId(command.restaurantId())
                .name(command.name())
                .description(command.description())
                .price(Money.of(command.price(), Currency.getInstance(command.currency())))
                .category(command.category())
                .idempotencyKey(command.idempotencyKey())
                .build();

        Product product = Product.create(data);
        Product productSaved = productRepository.save(product);

        return productSaved;
    }

    @Override
    public Product getProduct(UUID id) {
        return  productRepository.findById(id)
                .orElseThrow(()->new ProductNotFoundException(id));
    }

    @Override
    public List<Product> getProductsByRestaurantId(UUID restaurantId) {
        return productRepository.findByRestaurantId(restaurantId);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<ProductWithRestaurantView> getAllProductsWithRestaurant() {
        List<Product> products = productRepository.findAll();

        Set<UUID> restaurantIds = products.stream()
                .map(Product::getRestaurantId)
                .collect(Collectors.toSet());

        Map<UUID, Restaurant> restaurantsById = restaurantRepository.findAllById(restaurantIds).stream()
                .collect(Collectors.toMap(Restaurant::getId, Function.identity()));

        return products.stream()
                .map(p -> new ProductWithRestaurantView(p, restaurantsById.get(p.getRestaurantId())))
                .toList();
    }

}
