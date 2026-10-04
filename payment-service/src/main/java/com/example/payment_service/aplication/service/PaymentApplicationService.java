package com.example.payment_service.aplication.service;

import com.example.payment_service.aplication.port.in.ProcessPaymentCommand;
import com.example.payment_service.aplication.port.in.ProcessPaymentUseCase;
import com.example.payment_service.aplication.port.out.PaymentEventPublisherPort;
import com.example.payment_service.aplication.port.out.PaymentGatewayPort;
import com.example.payment_service.aplication.port.out.PaymentRepositoryPort;
import com.example.payment_service.aplication.port.out.UnitOfWork;
import com.example.payment_service.domain.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Currency;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class PaymentApplicationService implements ProcessPaymentUseCase {

    private final PaymentRepositoryPort paymentRepository;
    private final PaymentEventPublisherPort paymentEventPublisher;
    private final PaymentGatewayPort paymentGateway;
    private final UnitOfWork unitOfWork;

    @Override
    public Payment processPayment(ProcessPaymentCommand command) {

        Optional<Payment> existing = paymentRepository.findByOrderId(command.orderId());
        if (existing.isPresent()) {
            log.info("Ya existe un pago para el pedido {}, se ignora el reintento", command.orderId());
            return existing.get();
        }

        Currency currency = Currency.getInstance(command.currency());
        Money amount = Money.of(command.amount(), currency);

        NewPaymentData newPaymentData = NewPaymentData.builder()
                .orderId(command.orderId())
                .customerId(command.customerId())
                .restaurantId(command.restaurantId())
                .amount(amount)
                .method(PaymentMethod.valueOf(command.method()))
                .build();

        Payment payment = Payment.create(newPaymentData);

        boolean approved = paymentGateway.charge(amount, payment.getMethod());

        if (approved) {
            payment.confirm();
        } else {
            payment.fail("La pasarela de pago rechazó el cargo");
        }

        // Solo esta parte (guardar + publicar) es atómica — encerrada en la lambda para manejar una transaccion
        return unitOfWork.execute(() -> {
            Payment saved = paymentRepository.save(payment);

            if (saved.getStatus() == PaymentStatus.CONFIRMED) {
                paymentEventPublisher.publishPaymentConfirmed(saved);
            } else {
                paymentEventPublisher.publishPaymentFailed(saved);
            }

            return saved;
        });

    }
}
