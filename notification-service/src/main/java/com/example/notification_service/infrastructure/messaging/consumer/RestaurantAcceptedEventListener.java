package com.example.notification_service.infrastructure.messaging.consumer;

import com.example.notification_service.aplication.port.in.SendNotificationCommand;
import com.example.notification_service.aplication.port.in.SendNotificationUseCase;
import com.example.notification_service.domain.model.NotificationChannel;
import com.example.notification_service.infrastructure.messaging.event.RestaurantAcceptedEvent;
import lombok.RequiredArgsConstructor;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;

@RequiredArgsConstructor
public class RestaurantAcceptedEventListener {

    private final SendNotificationUseCase sendNotificationUseCase;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${app.kafka.topic.restaurant-accepted}", groupId = "notification-service")
    public void onRestaurantAccepted(String payload) throws JacksonException {
        RestaurantAcceptedEvent event = objectMapper.readValue(payload, RestaurantAcceptedEvent.class);

        SendNotificationCommand command = SendNotificationCommand.builder()
                .recipientId(event.customerId().toString())
                .eventType("restaurant.accepted")
                .aggregateId( event.orderId().toString())
                .channel(NotificationChannel.PUSH)
                .message("El restaurante aceptó tu pedido y ya lo está preparando")
                .build();

        sendNotificationUseCase.send(command);
    }
}