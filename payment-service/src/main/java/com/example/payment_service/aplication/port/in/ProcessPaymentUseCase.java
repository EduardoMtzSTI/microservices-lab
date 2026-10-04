package com.example.payment_service.aplication.port.in;

import com.example.payment_service.domain.model.Payment;

public interface ProcessPaymentUseCase {
    Payment processPayment(ProcessPaymentCommand command);
}
