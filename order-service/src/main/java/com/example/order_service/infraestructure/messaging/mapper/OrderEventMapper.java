package com.example.order_service.infraestructure.messaging.mapper;

import com.example.order_service.domain.model.Order;
import com.example.order_service.infraestructure.messaging.event.OrderCreatedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderEventMapper{

    public OrderCreatedEvent toEvent(Order order) {
        List<OrderCreatedEvent.OrderItemEvent> itemEvents = order.getItems().stream()
                .map(item -> OrderCreatedEvent.OrderItemEvent.builder()
                        .productName(item.productName())
                        .quantity(item.quantity())
                        .unitPrice(item.unitPrice().toDecimal())
                        .currency(item.unitPrice().currency().getCurrencyCode())
                        .build()
                ).toList();


        return OrderCreatedEvent.builder()
                .orderId(order.getId())
                .customerId(order.getCustomerId())
                .restaurantId(order.getRestaurantId())
                .totalAmount(order.getTotalAmount().toDecimal())
                .currency(order.getTotalAmount().currency().getCurrencyCode())
                .items(itemEvents)
                .occurredAt(order.getCreatedAt())
                .build();
    }
}
