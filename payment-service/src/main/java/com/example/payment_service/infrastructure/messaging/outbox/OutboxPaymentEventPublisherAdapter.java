package com.example.payment_service.infrastructure.messaging.outbox;

import com.example.payment_service.aplication.port.out.PaymentEventPublisherPort;
import com.example.payment_service.domain.model.Payment;
import com.example.payment_service.infrastructure.messaging.event.PaymentConfirmedEvent;
import com.example.payment_service.infrastructure.messaging.event.PaymentFailedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
public class OutboxPaymentEventPublisherAdapter implements PaymentEventPublisherPort {

    private final OutboxEventJpaRepository outboxRepository;
    private final ObjectMapper objectMapper;
    private final String paymentConfirmedTopic;
    private final String paymentFailedTopic;

    @Override
    public void publishPaymentConfirmed(Payment payment) {
        PaymentConfirmedEvent event = PaymentConfirmedEvent.builder()
                .id(payment.getId())
                .orderId(payment.getOrderId())
                .restaurantId(payment.getRestaurantId())
                .customerId(payment.getCustomerId())
                .createdAt(Instant.now())
                .build();
        saveToOutbox(paymentConfirmedTopic, "payment.confirmed", payment.getOrderId(), event);
    }

    @Override
    public void publishPaymentFailed(Payment payment) {
        PaymentFailedEvent event = PaymentFailedEvent.builder()
                .id(payment.getId())
                .orderId(payment.getOrderId())
                .customerId(payment.getCustomerId())
                .failureReason(payment.getFailureReason())
                .build();
        saveToOutbox(paymentFailedTopic, "payment.failed", payment.getOrderId(), event);
    }

    private void saveToOutbox(String topic, String eventType, UUID orderId, Object event) {
        String payload;
        try {
            payload = objectMapper.writeValueAsString(event);
        } catch (JacksonException e) {
            throw new IllegalStateException("No se pudo serializar el evento " + eventType, e);
        }

        outboxRepository.save(OutboxEventJpaEntity.builder()
                .id(UUID.randomUUID())
                .aggregateType("Payment")
                .aggregateId(orderId.toString())   // el orderId como key: mantiene el orden
                .eventType(eventType)               // por pedido en las particiones de Kafka
                .topic(topic)
                .messageKey(orderId.toString())
                .payload(payload)
                .createdAt(Instant.now())
                .build());

        log.info("Evento {} guardado en outbox para el pedido {}", eventType, orderId);
    }

}
