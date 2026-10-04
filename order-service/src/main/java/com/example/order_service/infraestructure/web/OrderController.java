package com.example.order_service.infraestructure.web;

import com.example.order_service.aplication.port.in.*;
import com.example.order_service.aplication.query.OrderWithCustomerView;
import com.example.order_service.domain.model.Order;
import com.example.order_service.infraestructure.web.dto.CreateOrderRequest;
import com.example.order_service.infraestructure.web.dto.OrderResponse;
import com.example.order_service.infraestructure.web.mapper.OrderWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;
    private final GetTodayRestaurantOrdersUseCase getTodayRestaurantOrdersUseCase;
    private final AcceptOrderUseCase acceptOrderUseCase;
    private final MarkOrderReadyForPickupUseCase markOrderReadyForPickupUseCase;
    private final OrderWebMapper orderWebMapper;

    @PostMapping
    public ResponseEntity<OrderResponse> create(
            @Valid @RequestBody CreateOrderRequest request,
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey
    ){
        CreateOrderCommand command = orderWebMapper.toCommand(request,idempotencyKey);
        Order createdOrder = createOrderUseCase.createOrder(command);
        OrderResponse orderResponse = orderWebMapper.toResponse(createdOrder);

        return ResponseEntity
                .created(URI.create("/api/orders/" + createdOrder.getId()))
                .body(orderResponse);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable UUID orderId) {
        Order order = getOrderUseCase.getOrder(orderId);
        return ResponseEntity.ok(orderWebMapper.toResponse(order));
    }

    @GetMapping("/restaurant/{restaurantId}/order/today")
    public ResponseEntity<List<OrderResponse>> getOrdersByStatus(@PathVariable UUID restaurantId){
        List<OrderWithCustomerView> orders = getTodayRestaurantOrdersUseCase.getTodayRestaurantOrders(restaurantId);
        List<OrderResponse> orderResponses = orders.stream()
                .map(orderWebMapper::toResponse)
                .toList();
        return ResponseEntity.ok(orderResponses);
    }

    @PatchMapping("/{id}/accept")
    public ResponseEntity<OrderResponse> acceptOrder(@PathVariable UUID id) {
        Order order = acceptOrderUseCase.accept(id);
        return ResponseEntity.ok(orderWebMapper.toResponse(order));
    }

    @PatchMapping("/{id}/ready")
    public ResponseEntity<OrderResponse> markReady(@PathVariable UUID id) {
        Order order = markOrderReadyForPickupUseCase.markReadyForPickup(id);
        return ResponseEntity.ok(orderWebMapper.toResponse(order));
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.status(HttpStatus.OK).body("order-service está funcionando correctamente");
    }

}
