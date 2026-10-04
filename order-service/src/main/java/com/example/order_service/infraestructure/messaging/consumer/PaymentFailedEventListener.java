package com.example.order_service.infraestructure.messaging.consumer;

import com.example.order_service.aplication.port.in.FailOrderPaymentUseCase;
import com.example.order_service.infraestructure.messaging.event.PaymentFailedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
public class PaymentFailedEventListener {

    private final FailOrderPaymentUseCase failOrderPaymentUseCase;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${app.kafka.topic.payment-failed}", groupId = "order-service")
    public void onPaymentFailed(String payload) throws JacksonException {
        PaymentFailedEvent event = objectMapper.readValue(payload, PaymentFailedEvent.class);
        failOrderPaymentUseCase.failPayment(event.orderId());
    }

}