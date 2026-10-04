package com.example.notification_service.infrastructure.messaging.consumer;

import com.example.notification_service.aplication.port.in.SendNotificationCommand;
import com.example.notification_service.aplication.port.in.SendNotificationUseCase;
import com.example.notification_service.domain.model.NotificationChannel;
import com.example.notification_service.infrastructure.messaging.event.DeliveryAssignedEvent;
import lombok.RequiredArgsConstructor;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;

@RequiredArgsConstructor
public class DeliveryAssignedEventListener {

    private final SendNotificationUseCase sendNotificationUseCase;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${app.kafka.topic.delivery-assigned}", groupId = "notification-service")
    public void onDeliveryAssigned(String payload) throws JacksonException {

        DeliveryAssignedEvent event = objectMapper.readValue(payload, DeliveryAssignedEvent.class);

        SendNotificationCommand command = SendNotificationCommand.builder()
                .recipientId(event.customerId().toString())
                .eventType("delivery.assigned")
                .aggregateId( event.orderId().toString())
                .channel(NotificationChannel.PUSH)
                .message(event.courierName() + " va en camino con tu pedido")
                .build();

        sendNotificationUseCase.send(command);
    }

}