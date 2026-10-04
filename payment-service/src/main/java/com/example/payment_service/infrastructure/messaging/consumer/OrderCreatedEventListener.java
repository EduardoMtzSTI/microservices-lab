package com.example.payment_service.infrastructure.messaging.consumer;

import com.example.payment_service.aplication.port.in.ProcessPaymentCommand;
import com.example.payment_service.aplication.port.in.ProcessPaymentUseCase;
import com.example.payment_service.infrastructure.messaging.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;

@RequiredArgsConstructor
@Slf4j
public class OrderCreatedEventListener {

    private final ProcessPaymentUseCase processPaymentUseCase;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "order.created", groupId = "payment-service")
    public void onOrderCreated(String payload) {

        OrderCreatedEvent event;

        try {
            event = objectMapper.readValue(payload, OrderCreatedEvent.class);
        } catch (JacksonException e) {
            log.error("No se pudo deserializar order.created, mensaje descartado: {}", payload, e);
            return;
        }

        log.info("order.created recibido para el pedido {}", event.orderId());

        ProcessPaymentCommand command = ProcessPaymentCommand.builder()
                .orderId(event.orderId())
                .customerId(event.customerId())
                .restaurantId(event.restaurantId())
                .amount(event.totalAmount())
                .currency(event.currency())
                .method("CREDIT_CARD")
                .build();

        processPaymentUseCase.processPayment(command);
    }

}
