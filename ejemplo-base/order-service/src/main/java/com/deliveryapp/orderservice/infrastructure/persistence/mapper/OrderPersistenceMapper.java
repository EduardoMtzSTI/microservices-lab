package com.deliveryapp.orderservice.infrastructure.persistence.mapper;

import com.deliveryapp.orderservice.domain.model.Money;
import com.deliveryapp.orderservice.domain.model.Order;
import com.deliveryapp.orderservice.domain.model.OrderItem;
import com.deliveryapp.orderservice.domain.model.OrderStatus;
import com.deliveryapp.orderservice.infrastructure.persistence.entity.OrderItemJpaEntity;
import com.deliveryapp.orderservice.infrastructure.persistence.entity.OrderJpaEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Traduce entre el mundo del dominio (Order, OrderItem, Money) y el mundo
 * de persistencia (OrderJpaEntity, OrderItemJpaEntity, BigDecimal). Este
 * mapper es, junto con el OrderWebMapper, uno de los dos puntos donde
 * "cruzan la frontera" los objetos del dominio — y ambos viven en infraestructura,
 * nunca en el dominio ni en la aplicación.
 */
@Component
public class OrderPersistenceMapper {

    public OrderJpaEntity toJpaEntity(Order order) {
        OrderJpaEntity entity = OrderJpaEntity.builder()
                .id(order.getId())
                .customerId(order.getCustomerId())
                .restaurantId(order.getRestaurantId())
                .totalAmount(order.getTotalAmount().amount())
                .status(order.getStatus().name())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .idempotencyKey(order.getIdempotencyKey())
                .build();

        List<OrderItemJpaEntity> itemEntities = order.getItems().stream()
                .map(item -> OrderItemJpaEntity.builder()
                        .productName(item.getProductName())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice().amount())
                        .order(entity)
                        .build())
                .toList();

        entity.setItems(itemEntities);
        return entity;
    }

    public Order toDomain(OrderJpaEntity entity) {
        List<OrderItem> items = entity.getItems().stream()
                .map(itemEntity -> new OrderItem(
                        itemEntity.getProductName(),
                        itemEntity.getQuantity(),
                        new Money(itemEntity.getUnitPrice())))
                .toList();

        return Order.reconstitute(
                entity.getId(),
                entity.getCustomerId(),
                entity.getRestaurantId(),
                items,
                new Money(entity.getTotalAmount()),
                OrderStatus.valueOf(entity.getStatus()),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getIdempotencyKey()
        );
    }
}
