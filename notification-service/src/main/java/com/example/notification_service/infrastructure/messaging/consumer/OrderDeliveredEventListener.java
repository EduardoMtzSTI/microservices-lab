package com.example.notification_service.infrastructure.messaging.consumer;

import com.example.notification_service.aplication.port.in.SendNotificationCommand;
import com.example.notification_service.aplication.port.in.SendNotificationUseCase;
import com.example.notification_service.domain.model.NotificationChannel;
import com.example.notification_service.infrastructure.messaging.event.OrderDeliveredEvent;
import lombok.RequiredArgsConstructor;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;

@RequiredArgsConstructor
public class OrderDeliveredEventListener {

    private final SendNotificationUseCase sendNotificationUseCase;
    private final ObjectMapper objectMapper;
    private static final String EVENT_TOPIC = "order.delivered";

    @KafkaListener(topics = "${app.kafka.topic.order-delivered}", groupId = "notification-service")
    public void onOrderDelivered(String payload) throws JacksonException {

        OrderDeliveredEvent event = objectMapper.readValue(payload, OrderDeliveredEvent.class);
        String orderId = event.orderId().toString();

        SendNotificationCommand emailNotificationCommand = SendNotificationCommand.builder()
                .recipientId(String.valueOf(event.customerId()))
                .eventType(EVENT_TOPIC)
                .aggregateId(orderId)
                .channel(NotificationChannel.EMAIL)
                .message("Tu pedido fue entregado, ¡buen provecho!")
                .build();

        sendNotificationUseCase.send(emailNotificationCommand);

        SendNotificationCommand sseNotificationCommand = SendNotificationCommand.builder()
                .recipientId(String.valueOf(event.customerId()))
                .eventType(EVENT_TOPIC)
                .aggregateId(orderId)
                .channel(NotificationChannel.SSE)
                .message("El pedido " + orderId + " fue entregado")
                .build();

        sendNotificationUseCase.send(sseNotificationCommand);
    }
}