package com.example.order_service.infraestructure.messaging.consumer;

import com.example.order_service.aplication.port.in.AcceptOrderUseCase;
import com.example.order_service.infraestructure.messaging.event.RestaurantAcceptedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
public class RestaurantAcceptedEventListener {

    private final AcceptOrderUseCase acceptOrderUseCase;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${app.kafka.topic.restaurant-accepted}", groupId = "order-service")
    public void onRestaurantAccepted(String payload) throws JacksonException {
        RestaurantAcceptedEvent event = objectMapper.readValue(payload, RestaurantAcceptedEvent.class);
        acceptOrderUseCase.accept(event.orderId());
    }

}