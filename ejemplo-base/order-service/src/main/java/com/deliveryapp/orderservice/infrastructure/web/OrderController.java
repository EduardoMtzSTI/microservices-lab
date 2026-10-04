package com.deliveryapp.orderservice.infrastructure.web;

import com.deliveryapp.orderservice.domain.model.Order;
import com.deliveryapp.orderservice.domain.port.in.CreateOrderCommand;
import com.deliveryapp.orderservice.domain.port.in.CreateOrderUseCase;
import com.deliveryapp.orderservice.domain.port.in.GetOrderUseCase;
import com.deliveryapp.orderservice.infrastructure.web.dto.CreateOrderRequest;
import com.deliveryapp.orderservice.infrastructure.web.dto.OrderResponse;
import com.deliveryapp.orderservice.infrastructure.web.mapper.OrderWebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

/**
 * Adaptador de entrada (driving adapter). Su única responsabilidad es traducir
 * HTTP <-> casos de uso del dominio.
 *
 * CORRECCIÓN respecto a la versión anterior: el controller ya NO depende de
 * OrderService (una clase concreta acoplada a JPA/Kafka), sino de dos
 * interfaces del dominio: CreateOrderUseCase y GetOrderUseCase. Esto es
 * Dependency Inversion aplicado en serio — Spring inyecta OrderApplicationService
 * porque es quien implementa esas interfaces, pero el controller ni se entera.
 * Además, ya no recibe ni devuelve la entidad Order de persistencia: solo
 * el objeto de dominio (vía el puerto) y el DTO (vía el mapper).
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;
    private final OrderWebMapper orderWebMapper;

    /**
     * Crea un nuevo pedido y dispara, de forma transaccional, el evento hacia el outbox
     * (que luego un proceso aparte publica en Kafka). Soporta idempotencia vía el
     * header estándar "Idempotency-Key": si el cliente reintenta la misma petición
     * (ej. por timeout), no se duplica el pedido.
     */
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody CreateOrderRequest request,
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey) {

        CreateOrderCommand command = orderWebMapper.toCommand(request, idempotencyKey);
        Order createdOrder = createOrderUseCase.createOrder(command);
        OrderResponse response = orderWebMapper.toResponse(createdOrder);

        return ResponseEntity
                .created(URI.create("/api/orders/" + createdOrder.getId()))
                .body(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable UUID orderId) {
        Order order = getOrderUseCase.getOrder(orderId);
        return ResponseEntity.ok(orderWebMapper.toResponse(order));
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.status(HttpStatus.OK).body("order-service está funcionando correctamente");
    }
}
