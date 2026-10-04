package com.example.payment_service.aplication.port.out;

import com.example.payment_service.domain.model.Money;
import com.example.payment_service.domain.model.PaymentMethod;

public interface PaymentGatewayPort {
    boolean charge(Money amount, PaymentMethod method);
}
