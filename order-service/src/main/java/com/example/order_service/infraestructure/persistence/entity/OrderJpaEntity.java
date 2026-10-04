package com.example.order_service.infraestructure.persistence.entity;

import com.example.order_service.domain.model.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

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
    private UUID customerId;

    @Column(nullable = false)
    private UUID restaurantId;

    @Column(nullable = false)
    private Long totalAmount;

    @Column(nullable = false)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    private Instant createdAt;

    private Instant updatedAt;

    @Column(unique = true)
    private String idempotencyKey;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItemJpaEntity> items = new ArrayList<>();

    public void addItem(OrderItemJpaEntity item) {
        items.add(item);
        item.setOrder(this);
    }
}
