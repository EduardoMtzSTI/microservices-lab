package com.example.notification_service.infrastructure.messaging.consumer;

import com.example.notification_service.aplication.port.in.SendNotificationCommand;
import com.example.notification_service.aplication.port.in.SendNotificationUseCase;
import com.example.notification_service.domain.model.NotificationChannel;
import com.example.notification_service.infrastructure.messaging.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;

@RequiredArgsConstructor
public class OrderCreatedEventListener {

    private final SendNotificationUseCase sendNotificationUseCase;
    private final ObjectMapper objectMapper;
    private static final String TOPIC_NAME = "${app.kafka.topic.order-created}";

    @KafkaListener(topics = TOPIC_NAME, groupId = "notification-service")
    public void onOrderCreated(String payload) throws JacksonException{

        OrderCreatedEvent event = objectMapper.readValue(payload,OrderCreatedEvent.class);
        String orderId = event.orderId().toString();

        SendNotificationCommand command = SendNotificationCommand.builder()
                .recipientId(String.valueOf(event.customerId()))
                .eventType(TOPIC_NAME)
                .aggregateId(orderId)
                .channel(NotificationChannel.SMS)
                .message("Nuevo pedido recivido: " + orderId)
                .build();

        sendNotificationUseCase.send(command);
    }

}
