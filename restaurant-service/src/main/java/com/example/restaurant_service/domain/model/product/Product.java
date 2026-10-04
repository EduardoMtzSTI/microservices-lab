package com.example.restaurant_service.domain.model.product;

import com.example.restaurant_service.domain.exception.InvalidProductException;
import com.example.restaurant_service.domain.validation.ProductValidator;
import com.example.restaurant_service.domain.validation.ValidationResult;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class Product {

    private final UUID id;
    private String name;
    private String description;
    private Money price;
    private final Instant createdAt;
    private ProductCategory category;
    private Instant updatedAt;
    private boolean availeble;
    private final UUID restaurantId;
    private final String idempotencyKey;

    private Product(ProductSnapshot data){
        this.id=data.id();
        this.name = data.name();
        this.description = data.description();
        this.restaurantId = data.restaurantId();
        this.price = data.price();
        this.availeble = data.available();
        this.category = data.category();
        this.createdAt = data.createdAt();
        this.updatedAt = data.updatedAt();
        this.idempotencyKey = data.idempotencyKey();
    }

    public static Product create(NewProductData data){

        ValidationResult validation = ProductValidator.all(
                ProductValidator.hasName(),
                ProductValidator.hasRestaurantId(),
                ProductValidator.hasCategory(),
                ProductValidator.isPriceValid()
        ).apply(data);

        if(!validation.isValid())
            throw new InvalidProductException(String.join(";",validation.errors()));

        Instant now = Instant.now();

        ProductSnapshot newProduct = ProductSnapshot.builder()
                .id(UUID.randomUUID())
                .name(data.name())
                .description(data.description())
                .restaurantId(data.restaurantId())
                .price(data.price())
                .category(data.category())
                .available(true)
                .createdAt(now)
                .updatedAt(now)
                .idempotencyKey(data.idempotencyKey())
                .build();

        return new Product(newProduct);
    }

    public static Product reconstitute(ProductSnapshot data){
        return new Product(data);
    }

}
