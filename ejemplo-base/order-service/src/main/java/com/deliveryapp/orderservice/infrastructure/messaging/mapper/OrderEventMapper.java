package com.deliveryapp.orderservice.infrastructure.messaging.mapper;

import com.deliveryapp.orderservice.domain.model.Order;
import com.deliveryapp.orderservice.infrastructure.messaging.event.OrderCreatedEvent;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class OrderEventMapper {

    public OrderCreatedEvent toEvent(Order order) {
        List<OrderCreatedEvent.OrderItemEvent> itemEvents = order.getItems().stream()
                .map(item -> new OrderCreatedEvent.OrderItemEvent(
                        item.getProductName(), item.getQuantity(), item.getUnitPrice().amount()))
                .toList();

        return new OrderCreatedEvent(
                order.getId(),
                order.getCustomerId(),
                order.getRestaurantId(),
                order.getTotalAmount().amount(),
                itemEvents,
                Instant.now()
        );
    }
}
