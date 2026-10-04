package com.example.order_service.infraestructure.persistence.mapper;

import com.example.order_service.domain.model.Money;
import com.example.order_service.domain.model.Order;
import com.example.order_service.domain.model.OrderItem;
import com.example.order_service.domain.model.OrderSnapshot;
import com.example.order_service.infraestructure.persistence.entity.OrderItemJpaEntity;
import com.example.order_service.infraestructure.persistence.entity.OrderJpaEntity;
import org.springframework.stereotype.Component;

import java.util.Currency;
import java.util.List;

@Component
public class OrderPersistenceMapper {

    public OrderJpaEntity toJpaEntity(Order order) {
        OrderJpaEntity entity = OrderJpaEntity.builder()
                .id(order.getId())
                .customerId(order.getCustomerId())
                .restaurantId(order.getRestaurantId())
                .totalAmount(order.getTotalAmount().amount())
                .currency(order.getTotalAmount().currency().getCurrencyCode())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .idempotencyKey(order.getIdempotencyKey())
                .build();

        order.getItems().forEach(item -> {
            OrderItemJpaEntity itemEntity = OrderItemJpaEntity.builder()
                    .productId(item.productId())
                    .productName(item.productName())
                    .unitPrice(item.unitPrice().amount())
                    .currency(item.unitPrice().currency().getCurrencyCode())
                    .quantity(item.quantity())
                    .build();

            entity.addItem(itemEntity);
        });
        return entity;
    }

    public Order toDomain(OrderJpaEntity entity) {
        List<OrderItem> items = entity.getItems().stream()
                .map(itemEntity -> OrderItem.builder()
                        .productId(itemEntity.getProductId())
                        .productName(itemEntity.getProductName())
                        .quantity(itemEntity.getQuantity())
                        .unitPrice(
                                new Money(
                                        itemEntity.getUnitPrice(),
                                        Currency.getInstance(entity.getCurrency()))
                        )
                        .build()
                )
                .toList();

        OrderSnapshot orderSnapshot =  OrderSnapshot.builder()
                .id(entity.getId())
                .customerId(entity.getCustomerId())
                .restaurantId(entity.getRestaurantId())
                .items(items)
                .totalAmount(
                        new Money(
                            entity.getTotalAmount(),
                            Currency.getInstance(entity.getCurrency()))
                )
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .idempotencyKey(entity.getIdempotencyKey())
                .build();

        return Order.reconstitute(orderSnapshot);
    }

}
