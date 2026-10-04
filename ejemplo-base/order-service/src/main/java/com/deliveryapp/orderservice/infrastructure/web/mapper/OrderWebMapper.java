package com.deliveryapp.orderservice.infrastructure.web.mapper;

import com.deliveryapp.orderservice.domain.model.Order;
import com.deliveryapp.orderservice.domain.port.in.CreateOrderCommand;
import com.deliveryapp.orderservice.infrastructure.web.dto.CreateOrderRequest;
import com.deliveryapp.orderservice.infrastructure.web.dto.OrderResponse;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Responsabilidad única: traducir entre el "idioma" del API REST y el
 * "idioma" del dominio/aplicación. Ni el Controller ni el Service hacen
 * este mapeo directamente — así cada uno se mantiene enfocado en su propia tarea.
 */
@Component
public class OrderWebMapper {

    public CreateOrderCommand toCommand(CreateOrderRequest request, String idempotencyKey) {
        List<CreateOrderCommand.OrderItemCommand> items = request.getItems().stream()
                .map(item -> new CreateOrderCommand.OrderItemCommand(
                        item.getProductName(), item.getQuantity(), item.getUnitPrice()))
                .toList();

        return new CreateOrderCommand(request.getCustomerId(), request.getRestaurantId(), items, idempotencyKey);
    }

    public OrderResponse toResponse(Order order) {
        List<String> itemDescriptions = order.getItems().stream()
                .map(item -> "%dx %s".formatted(item.getQuantity(), item.getProductName()))
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getCustomerId(),
                order.getRestaurantId(),
                order.getTotalAmount().amount(),
                order.getStatus().name(),
                order.getCreatedAt(),
                itemDescriptions
        );
    }
}
