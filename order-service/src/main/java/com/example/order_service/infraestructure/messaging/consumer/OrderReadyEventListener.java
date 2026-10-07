package com.example.order_service.infraestructure.messaging.consumer;

import com.example.order_service.aplication.port.in.MarkOrderReadyForPickupUseCase;
import com.example.order_service.infraestructure.messaging.event.OrderReadyEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
public class OrderReadyEventListener {

    private final MarkOrderReadyForPickupUseCase markOrderReadyForPickupUseCase;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${app.kafka.topic.order-ready}", groupId = "order-service")
    public void onOrderReady(String payload) throws JacksonException {
        OrderReadyEvent event = objectMapper.readValue(payload,OrderReadyEvent.class);
        markOrderReadyForPickupUseCase.markReadyForPickup(event.orderId());
    }
}
