package com.example.order_service.infraestructure.messaging.consumer;

import com.example.order_service.aplication.port.in.ConfirmOrderPaymentUseCase;
import com.example.order_service.infraestructure.messaging.event.PaymentConfirmedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
public class PaymentConfirmedEventListener {

    private final ConfirmOrderPaymentUseCase confirmOrderPaymentUseCase;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${app.kafka.topic.payment-confirmed}", groupId = "order-service")
    public void onPaymentConfirmed(String payload) throws JacksonException {
        PaymentConfirmedEvent event = objectMapper.readValue(payload, PaymentConfirmedEvent.class);
        confirmOrderPaymentUseCase.confirmPayment(event.orderId());
    }

}
