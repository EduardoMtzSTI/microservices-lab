package com.example.payment_service.infrastructure.persistence.adapter;

import com.example.payment_service.aplication.port.out.PaymentRepositoryPort;
import com.example.payment_service.domain.model.Payment;
import com.example.payment_service.infrastructure.persistence.entity.PaymentJpaEntity;
import com.example.payment_service.infrastructure.persistence.mapper.PaymentPersistenceMapper;
import com.example.payment_service.infrastructure.persistence.repository.SpringDataPaymentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class PaymentRepositoryAdapter implements PaymentRepositoryPort {

    private final SpringDataPaymentJpaRepository jpaRepository;
    private final PaymentPersistenceMapper mapper;

    @Override
    public Payment save(Payment payment) {
        PaymentJpaEntity paymentSaved = jpaRepository.save(mapper.toJpaEntity(payment));
        return mapper.toDomain(paymentSaved);
    }

    @Override
    public Optional<Payment> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Payment> findByOrderId(UUID orderId) {
        return jpaRepository.findByOrderId(orderId).map(mapper::toDomain);
    }

}