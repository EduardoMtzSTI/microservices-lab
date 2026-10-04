package com.example.restaurant_service.infraestructure.persistence.mapper;

import com.example.restaurant_service.domain.model.product.Money;
import com.example.restaurant_service.domain.model.product.Product;
import com.example.restaurant_service.domain.model.product.ProductCategory;
import com.example.restaurant_service.domain.model.product.ProductSnapshot;
import com.example.restaurant_service.infraestructure.persistence.entity.ProductJpaEntity;
import org.springframework.stereotype.Component;

import java.util.Currency;

@Component
public class ProductPersistenceMapper {

    public ProductJpaEntity toJpaEntity(Product product){
        return ProductJpaEntity.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice().amount())
                .currency(product.getPrice().currency().getCurrencyCode())
                .description(product.getDescription())
                .available(product.isAvaileble())
                .restaurantId(product.getRestaurantId())
                .category(product.getCategory().name())
                .updatedAt(product.getUpdatedAt())
                .createdAt(product.getCreatedAt())
                .idempotencyKey(product.getIdempotencyKey())
                .build();
    }

    public Product toDomain(ProductJpaEntity entity){

        ProductSnapshot data = ProductSnapshot.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(
                        new Money(
                            entity.getPrice(),
                            Currency.getInstance(entity.getCurrency()))
                 )
                .available(entity.isAvailable())
                .category(ProductCategory.valueOf(entity.getCategory()))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .restaurantId(entity.getRestaurantId())
                .idempotencyKey(entity.getIdempotencyKey())
                .build();

        return Product.reconstitute(data);
    }
}
