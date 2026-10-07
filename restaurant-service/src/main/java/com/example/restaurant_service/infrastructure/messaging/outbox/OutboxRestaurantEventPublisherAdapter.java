package com.example.restaurant_service.infrastructure.messaging.outbox;

import com.example.restaurant_service.aplication.port.out.EventPublisherPort;
import com.example.restaurant_service.infrastructure.messaging.event.OrderAcceptedEvent;
import com.example.restaurant_service.infrastructure.messaging.event.OrderReadyEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OutboxRestaurantEventPublisherAdapter implements EventPublisherPort {

    private final OutboxEventJpaRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @Value("${app.kafka.topic.order-accepted}")
    private String orderAcceptedTopic;

    @Value("${app.kafka.topic.order-ready}")
    private String orderReadyTopic;

    @Override
    public void publishRestaurantAccepted(UUID restaurantId, UUID orderId) {
        String payload;
        try{
            OrderAcceptedEvent event = OrderAcceptedEvent.builder()
                    .orderId(orderId)
                    .restaurantId(restaurantId)
                    .occurredAt(Instant.now())
                    .build();
            payload = objectMapper.writeValueAsString(event);
        }catch (JacksonException e){
            throw new IllegalStateException("No se pudo serializar el evento "+ orderAcceptedTopic, e);
        }
        OutboxEventJpaEntity outboxEvent = OutboxEventJpaEntity.builder()
                .id(UUID.randomUUID())
                .aggregateType("RestaurantOrderReview")
                .aggregateId(orderId.toString())
                .eventType("order.accepted")
                .topic(orderAcceptedTopic)
                .messageKey(orderId.toString())
                .payload(payload)
                .createdAt(Instant.now())
                .build();
        outboxRepository.save(outboxEvent);
    }

    @Override
    public void publishMarkReadyForPickup(UUID restaurantId, UUID orderId) {
        String payload;
        try{
            OrderReadyEvent event = OrderReadyEvent.builder()
                    .orderId(orderId)
                    .restaurantId(restaurantId)
                    .occurredAt(Instant.now())
                    .build();
            payload = objectMapper.writeValueAsString(event);
        }catch (JacksonException e){
            throw new IllegalStateException("No se pudo serializar el evento "+ orderReadyTopic, e);
        }
        OutboxEventJpaEntity outboxEvent = OutboxEventJpaEntity.builder()
                .id(UUID.randomUUID())
                .aggregateType("RestaurantOrderReview")
                .aggregateId(orderId.toString())
                .eventType("order.ready")
                .topic(orderReadyTopic)
                .messageKey(orderId.toString())
                .payload(payload)
                .createdAt(Instant.now())
                .build();
        outboxRepository.save(outboxEvent);
    }
}
