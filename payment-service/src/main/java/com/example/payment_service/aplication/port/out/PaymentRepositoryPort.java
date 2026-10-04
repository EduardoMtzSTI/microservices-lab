package com.example.payment_service.aplication.port.out;

import com.example.payment_service.domain.model.Payment;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepositoryPort {
    Payment save(Payment payment);
    Optional<Payment> findById(UUID id);
    Optional<Payment> findByOrderId(UUID orderId);
}
