package com.example.order_service.infraestructure.web.mapper;

import com.example.order_service.aplication.port.in.CreateOrderCommand;
import com.example.order_service.aplication.port.out.CustomerSummary;
import com.example.order_service.aplication.query.OrderWithCustomerView;
import com.example.order_service.domain.model.Order;
import com.example.order_service.infraestructure.web.dto.CreateOrderRequest;
import com.example.order_service.infraestructure.web.dto.CustomerSummaryResponse;
import com.example.order_service.infraestructure.web.dto.ItemResponse;
import com.example.order_service.infraestructure.web.dto.OrderResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class OrderWebMapper {

    public CreateOrderCommand toCommand(CreateOrderRequest request ,String idempotencyKey){

        List<CreateOrderCommand.OrderItemCommand> items = request.items().stream()
                .map(item -> CreateOrderCommand.OrderItemCommand.builder()
                        .id(UUID.fromString(item.id()))
                        .quantity(item.quantity())
                        .build())
                .toList();

        return CreateOrderCommand.builder()
                .customerId(UUID.fromString(request.customerId()))
                .restaurantId(UUID.fromString((request.restaurantId())))
                .items(items)
                .idempotencyKey(idempotencyKey)
                .build();
    }

    public OrderResponse toResponse(Order order){

        List<ItemResponse> itemDescriptions = order.getItems().stream()
                .map(item ->
                        ItemResponse.builder()
                            .productId(item.productId())
                            .productName(item.productName())
                            .quantity(item.quantity())
                            .unitPrice(item.unitPrice().toDecimal())
                            .build()
                )
                .toList();

        return OrderResponse.builder()
                .id(order.getId())
                .customer(
                        CustomerSummaryResponse.builder()
                                .id(order.getCustomerId())
                                .build()
                )
                .restaurantId(order.getRestaurantId())
                .totalAmount(order.getTotalAmount().toDecimal())
                .currency(order.getTotalAmount().currency().getCurrencyCode())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .items(itemDescriptions)
                .build();
    }

    public OrderResponse toResponse(OrderWithCustomerView owcv) {

        Order order = owcv.order();
        CustomerSummary customerSummary = owcv.customer();

        CustomerSummaryResponse customerSR = CustomerSummaryResponse.builder()
                .id(customerSummary.id())
                .phone(customerSummary.phone())
                .fullName(customerSummary.fullName())
                .address(customerSummary.address())
                .build();

        List<ItemResponse> itemDescriptions = order.getItems().stream()
                .map(item ->
                        ItemResponse.builder()
                                .productId(item.productId())
                                .productName(item.productName())
                                .quantity(item.quantity())
                                .unitPrice(item.unitPrice().toDecimal())
                                .build()
                )
                .toList();

        return OrderResponse.builder()
                .id(order.getId())
                .restaurantId(order.getRestaurantId())
                .customer(customerSR)
                .totalAmount(order.getTotalAmount().toDecimal())
                .currency(order.getTotalAmount().currency().getCurrencyCode())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .items(itemDescriptions)
                .build();
    }
}
