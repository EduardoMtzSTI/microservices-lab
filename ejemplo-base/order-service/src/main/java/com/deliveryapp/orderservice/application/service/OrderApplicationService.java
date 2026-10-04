package com.deliveryapp.orderservice.application.service;

import com.deliveryapp.orderservice.domain.exception.OrderNotFoundException;
import com.deliveryapp.orderservice.domain.model.Money;
import com.deliveryapp.orderservice.domain.model.Order;
import com.deliveryapp.orderservice.domain.model.OrderItem;
import com.deliveryapp.orderservice.domain.port.in.CreateOrderCommand;
import com.deliveryapp.orderservice.domain.port.in.CreateOrderUseCase;
import com.deliveryapp.orderservice.domain.port.in.GetOrderUseCase;
import com.deliveryapp.orderservice.domain.port.out.OrderEventPublisherPort;
import com.deliveryapp.orderservice.domain.port.out.OrderRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementación de los casos de uso. Es la ÚNICA clase de toda la aplicación
 * que orquesta "guardar pedido" + "publicar evento" como una sola operación
 * de negocio — y por eso es también el único lugar correcto para el límite
 * transaccional (@Transactional).
 *
 * Nótese que esta clase solo conoce interfaces (los puertos), nunca JPA ni
 * Kafka directamente. Eso es lo que hace posible, por ejemplo, testearla con
 * un OrderRepositoryPort falso en memoria sin levantar Spring ni una base de datos real.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderApplicationService implements CreateOrderUseCase, GetOrderUseCase {

    private final OrderRepositoryPort orderRepository;
    private final OrderEventPublisherPort orderEventPublisher;

    @Override
    @Transactional
    public Order createOrder(CreateOrderCommand command) {
        // Idempotencia: si el cliente reintenta la misma petición (ej. por timeout de red)
        // usando la misma Idempotency-Key, devolvemos el pedido ya creado en vez de duplicarlo.
        if (command.idempotencyKey() != null && !command.idempotencyKey().isBlank()) {
            Optional<Order> existingOrder = orderRepository.findByIdempotencyKey(command.idempotencyKey());
            if (existingOrder.isPresent()) {
                log.info("Idempotency-Key {} ya procesada, devolviendo el pedido existente {}",
                        command.idempotencyKey(), existingOrder.get().getId());
                return existingOrder.get();
            }
        }

        List<OrderItem> items = command.items().stream()
                .map(item -> new OrderItem(item.productName(), item.quantity(), Money.of(item.unitPrice())))
                .toList();

        Order order = Order.create(command.customerId(), command.restaurantId(), items, command.idempotencyKey());

        Order savedOrder = orderRepository.save(order);

        // Al estar dentro del mismo método @Transactional, el adaptador de outbox
        // guarda el evento en la misma transacción de BD que el pedido: o se
        // confirman los dos juntos, o no se confirma ninguno (ver OutboxOrderEventPublisherAdapter).
        orderEventPublisher.publishOrderCreated(savedOrder);

        return savedOrder;
    }

    @Override
    public Order getOrder(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }
}
