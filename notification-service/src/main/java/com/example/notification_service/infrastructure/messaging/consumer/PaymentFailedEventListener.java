package com.example.notification_service.infrastructure.messaging.consumer;

import com.example.notification_service.aplication.port.in.SendNotificationCommand;
import com.example.notification_service.aplication.port.in.SendNotificationUseCase;
import com.example.notification_service.domain.model.NotificationChannel;
import com.example.notification_service.infrastructure.messaging.event.PaymentFailedEvent;
import lombok.RequiredArgsConstructor;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;

@RequiredArgsConstructor
public class PaymentFailedEventListener {

    private final SendNotificationUseCase sendNotificationUseCase;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${app.kafka.topic.payment-failed}", groupId = "notification-service")
    public void onPaymentFailed(String payload) throws JacksonException {
        PaymentFailedEvent event = objectMapper.readValue(payload, PaymentFailedEvent.class);

        SendNotificationCommand command = SendNotificationCommand.builder()
                .recipientId(event.customerId().toString())
                .eventType("payment.failed")
                .aggregateId(event.orderId().toString())
                .channel(NotificationChannel.SSE)
                .message("No pudimos procesar tu pago: " + event.reason())
                .build();

        sendNotificationUseCase.send(command);
    }

}