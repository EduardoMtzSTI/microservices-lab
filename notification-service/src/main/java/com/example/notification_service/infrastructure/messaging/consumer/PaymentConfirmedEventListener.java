package com.example.notification_service.infrastructure.messaging.consumer;

import com.example.notification_service.aplication.port.in.SendNotificationCommand;
import com.example.notification_service.aplication.port.in.SendNotificationUseCase;
import com.example.notification_service.domain.model.NotificationChannel;
import com.example.notification_service.infrastructure.messaging.event.PaymentConfirmedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
public class PaymentConfirmedEventListener {

    private final SendNotificationUseCase sendNotificationUseCase;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${app.kafka.topic.payment-confirmed}", groupId = "notification-service")
    public void onPaymentConfirmed(String payload) throws JacksonException {

        PaymentConfirmedEvent event = objectMapper.readValue(payload, PaymentConfirmedEvent.class);

        SendNotificationCommand customerCommand = SendNotificationCommand.builder()
                .recipientId(event.customerId().toString())
                .eventType("payment.confirmed")
                .aggregateId(event.orderId().toString())
                .channel(NotificationChannel.SSE)
                .message("Tu pago fue confirmado")
                .build();

        sendNotificationUseCase.send(customerCommand);

        SendNotificationCommand restaurantCommand = SendNotificationCommand.builder()
                .recipientId(event.restaurantId().toString())
                .eventType("payment.confirmed")
                .aggregateId(event.orderId().toString())
                .channel(NotificationChannel.SSE)
                .message("Nueva orden")
                .build();

        sendNotificationUseCase.send(restaurantCommand);
    }

}
