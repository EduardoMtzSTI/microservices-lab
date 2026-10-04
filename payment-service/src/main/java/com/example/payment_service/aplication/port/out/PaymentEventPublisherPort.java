package com.example.payment_service.aplication.port.out;

import com.example.payment_service.domain.model.Payment;

public interface PaymentEventPublisherPort {
    void publishPaymentConfirmed(Payment payment);
    void publishPaymentFailed(Payment payment);
}
