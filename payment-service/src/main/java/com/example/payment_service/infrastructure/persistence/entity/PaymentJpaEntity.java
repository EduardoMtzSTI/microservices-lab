package com.example.payment_service.infrastructure.persistence.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID orderId;

    @Column(nullable = false)
    private UUID customerId;

    @Column(nullable = false)
    private UUID restaurantId;

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false, length = 30)
    private String method;

    @Column(nullable = false, length = 30)
    private String status;

    @Column(length = 255)
    private String failureReason;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    private Instant updatedAt;
}
