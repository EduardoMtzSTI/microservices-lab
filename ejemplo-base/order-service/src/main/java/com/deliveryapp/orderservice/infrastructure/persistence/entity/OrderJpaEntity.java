package com.deliveryapp.orderservice.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String customerId;

    @Column(nullable = false)
    private String restaurantId;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Instant createdAt;

    private Instant updatedAt;

    // Único y nullable: permite que la mayoría de los pedidos no manden Idempotency-Key,
    // pero si la mandan, la BD garantiza a nivel de constraint que nunca hay dos pedidos
    // con la misma key (defensa adicional además de la revisión en el service).
    @Column(unique = true)
    private String idempotencyKey;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItemJpaEntity> items = new ArrayList<>();
}
