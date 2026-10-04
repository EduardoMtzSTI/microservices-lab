package com.deliveryapp.orderservice.infrastructure.messaging.outbox;

import com.deliveryapp.orderservice.domain.model.Order;
import com.deliveryapp.orderservice.domain.port.out.OrderEventPublisherPort;
import com.deliveryapp.orderservice.infrastructure.messaging.event.OrderCreatedEvent;
import com.deliveryapp.orderservice.infrastructure.messaging.mapper.OrderEventMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

/**
 * Implementa OrderEventPublisherPort SIN llamar a Kafka directamente.
 * En vez de eso, serializa el evento y lo guarda como una fila en la tabla
 * "outbox_events". Como este método se invoca desde dentro del mismo
 * @Transactional del caso de uso (OrderApplicationService.createOrder),
 * el INSERT del pedido y el INSERT del evento outbox ocurren en la misma
 * transacción de base de datos: ambos se confirman juntos, o ninguno.
 *
 * La publicación real hacia Kafka la hace un proceso aparte y asíncrono:
 * ver OutboxRelayScheduler.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxOrderEventPublisherAdapter implements OrderEventPublisherPort {

    private final OutboxEventJpaRepository outboxRepository;
    private final OrderEventMapper orderEventMapper;
    private final ObjectMapper objectMapper;

    @Value("${app.kafka.topic.order-created}")
    private String orderCreatedTopic;

    @Override
    public void publishOrderCreated(Order order) {
        OrderCreatedEvent event = orderEventMapper.toEvent(order);

        String payload;
        try {
            payload = objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            // Si ni siquiera podemos serializar el evento, es un bug de programación,
            // no un problema transitorio de red: mejor fallar rápido y hacer rollback
            // de todo (incluyendo el pedido) que dejar un pedido sin evento asociado.
            throw new IllegalStateException("No se pudo serializar el evento order.created", e);
        }

        OutboxEventJpaEntity outboxEvent = OutboxEventJpaEntity.builder()
                .id(UUID.randomUUID())
                .aggregateType("Order")
                .aggregateId(order.getId().toString())
                .eventType("order.created")
                .topic(orderCreatedTopic)
                .messageKey(order.getId().toString())
                .payload(payload)
                .createdAt(Instant.now())
                .build();

        outboxRepository.save(outboxEvent);
        log.info("Evento order.created guardado en outbox para el pedido {} (pendiente de publicar en Kafka)",
                order.getId());
    }
}
