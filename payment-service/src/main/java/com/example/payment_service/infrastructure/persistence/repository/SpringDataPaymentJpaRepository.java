package com.example.payment_service.infrastructure.persistence.repository;

import com.example.payment_service.infrastructure.persistence.entity.PaymentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataPaymentJpaRepository extends JpaRepository<PaymentJpaEntity, UUID> {
    Optional<PaymentJpaEntity> findByOrderId(UUID orderId);
}
