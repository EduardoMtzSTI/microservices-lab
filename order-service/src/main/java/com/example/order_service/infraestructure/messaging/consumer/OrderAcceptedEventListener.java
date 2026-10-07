package com.example.order_service.infraestructure.messaging.consumer;

import com.example.order_service.aplication.port.in.AcceptOrderUseCase;
import com.example.order_service.infraestructure.messaging.event.OrderAcceptedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
public class OrderAcceptedEventListener {

    private final AcceptOrderUseCase acceptOrderUseCase;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${app.kafka.topic.order-accepted}", groupId = "order-service")
    public void onRestaurantAccepted(String payload) throws JacksonException {
        OrderAcceptedEvent event = objectMapper.readValue(payload, OrderAcceptedEvent.class);
        acceptOrderUseCase.accept(event.orderId());
    }

}