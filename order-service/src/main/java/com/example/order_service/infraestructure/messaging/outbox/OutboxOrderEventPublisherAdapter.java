package com.example.order_service.infraestructure.messaging.outbox;

import com.example.order_service.aplication.port.out.OrderEventPublisherPort;
import com.example.order_service.domain.model.Order;
import com.example.order_service.infraestructure.messaging.event.OrderCreatedEvent;
import com.example.order_service.infraestructure.messaging.event.OrderStatusChangedEvent;
import com.example.order_service.infraestructure.messaging.mapper.OrderEventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OutboxOrderEventPublisherAdapter  implements OrderEventPublisherPort {

    private final OutboxEventJpaRepository outboxRepository;
    private final OrderEventMapper orderEventMapper;
    private final ObjectMapper objectMapper;

    @Value("${app.kafka.topic.order-created}")
    private String orderCreatedTopic;
    @Value("${app.kafka.topic.order-status-changed}")
    private String orderStatusChangedTopic;

    @Override
    public void publishOrderCreated(Order order) {
        OrderCreatedEvent event = orderEventMapper.toOrderCreatedEvent(order);
        String payload;
        try {
            payload = objectMapper.writeValueAsString(event);
        } catch (JacksonException e) {
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
    }

    @Override
    public void publishOrderStatusChange(Order order) {
        OrderStatusChangedEvent event = orderEventMapper.toOrderStatusChangedEvent(order);
        String payload;
        try {
            payload = objectMapper.writeValueAsString(event);
        } catch (JacksonException e) {
            throw new IllegalStateException("No se pudo serializar el evento order.created", e);
        }
        OutboxEventJpaEntity outboxEvent = OutboxEventJpaEntity.builder()
                .id(UUID.randomUUID())
                .aggregateType("Order")
                .aggregateId(order.getId().toString())
                .eventType("order-status-changed")
                .topic(orderStatusChangedTopic)
                .messageKey(order.getId().toString())
                .payload(payload)
                .createdAt(Instant.now())
                .build();

        outboxRepository.save(outboxEvent);
    }
}
