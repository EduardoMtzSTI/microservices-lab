package com.example.restaurant_service.infraestructure.persistence.entity;

import com.example.restaurant_service.domain.model.product.ProductCategory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID restaurantId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    private String currency;

    private String description;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private boolean available;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    private Instant updatedAt;

    private String idempotencyKey;

}
