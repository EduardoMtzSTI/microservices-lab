package com.example.notification_service.infrastructure.messaging.consumer;


import com.example.notification_service.aplication.port.in.SendNotificationCommand;
import com.example.notification_service.aplication.port.in.SendNotificationUseCase;
import com.example.notification_service.domain.model.NotificationChannel;
import com.example.notification_service.infrastructure.messaging.event.OrderStatusChangedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
public class OrderStatusChangedListener {

    private final SendNotificationUseCase sendNotificationUseCase;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${app.kafka.topic.order-status-changed}", groupId = "notification-service")
    public void onOrderChangeStatus(String payload) throws JacksonException {

        OrderStatusChangedEvent event = objectMapper.readValue(payload, OrderStatusChangedEvent.class);
        String evenType = "order-status-changed-to-["+event.newStatus()+"]";
        SendNotificationCommand restaurantCommand = SendNotificationCommand.builder()
                .recipientId(event.restaurantId().toString())
                .eventType(evenType)
                .aggregateId(event.orderId().toString())
                .channel(NotificationChannel.SSE)
                .message("Cambio de estatus de orden a:"+event.newStatus())
                .build();

        sendNotificationUseCase.send(restaurantCommand);
    }
}
