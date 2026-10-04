package com.example.payment_service.infrastructure.gateway;

import com.example.payment_service.aplication.port.out.PaymentGatewayPort;
import com.example.payment_service.domain.model.Money;
import com.example.payment_service.domain.model.PaymentMethod;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class SimulatedPaymentGatewayAdapter implements PaymentGatewayPort {

    private static final double APPROVAL_RATE = 0.90;

    @Override
    public boolean charge(Money amount, PaymentMethod method) {
        simulateNetworkLatency();

        boolean approved = ThreadLocalRandom.current().nextDouble() < APPROVAL_RATE;

        log.info("Cargo simulado por {} {} vía {} -> {}",
                amount.toDecimal(), amount.currency().getCurrencyCode(), method,
                approved ? "APROBADO" : "RECHAZADO");

        return approved;
    }

    private void simulateNetworkLatency() {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(200, 800));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
