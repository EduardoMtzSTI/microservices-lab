package com.example.order_service.aplication.service;

import com.example.order_service.aplication.exception.OrderNotFoundException;
import com.example.order_service.aplication.port.in.*;
import com.example.order_service.aplication.port.out.*;
import com.example.order_service.aplication.query.OrderWithCustomerView;
import com.example.order_service.domain.model.NewOrderData;
import com.example.order_service.domain.model.Order;
import com.example.order_service.domain.model.OrderItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
public class OrderApplicationService
        implements
        CreateOrderUseCase,
        GetOrderUseCase,
        ConfirmOrderPaymentUseCase,
        FailOrderPaymentUseCase,
        AcceptOrderUseCase,
        MarkOrderReadyForPickupUseCase,
        GetTodayRestaurantOrdersUseCase
{
    private final OrderRepositoryPort orderRepository;
    private final ProductCatalogPort productCatalog;
    private final OrderEventPublisherPort orderEventPublisher;
    private final CustomerLookupPort customerLookup;
    private final UnitOfWork unitOfWork;

    @Override
    public Order createOrder(CreateOrderCommand command) {

        if (command.idempotencyKey() != null && !command.idempotencyKey().isBlank()) {
            Optional<Order> existingOrder = orderRepository.findByIdempotencyKey(command.idempotencyKey());
            if (existingOrder.isPresent()) {
                log.info("Idempotency-Key {} ya procesada, devolviendo el pedido existente {}",
                        command.idempotencyKey(), existingOrder.get().getId());
                return existingOrder.get();
            }
        }

        List<OrderItem> items = command.items().stream()
                .map(item ->{
                    ProductPrice productPrice = productCatalog.getCurrentPrice(item.id());
                    return OrderItem.builder()
                            .productId(productPrice.productId())
                            .productName(productPrice.name())
                            .quantity(item.quantity())
                            .unitPrice(productPrice.price())
                            .build();
                })
                .toList();

        NewOrderData data = NewOrderData.builder()
                .customerId(command.customerId())
                .restaurantId(command.restaurantId())
                .items(items)
                .idempotencyKey(command.idempotencyKey())
                .build();

        Order order = Order.create(data);
        Order orderSaved = orderRepository.save(order);
        orderEventPublisher.publishOrderCreated(orderSaved);

        return orderSaved;
    }

    @Override
    public Order getOrder(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    @Override
    public List<OrderWithCustomerView> getTodayRestaurantOrders(UUID restaurantId) {

        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate today = LocalDate.now(zoneId);

        Instant start = today
                .atStartOfDay(zoneId)
                .toInstant();

        Instant end = today
                .plusDays(1)
                .atStartOfDay(zoneId)
                .toInstant();

        List<Order> orders = orderRepository.findByRestaurantIdAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
                restaurantId, start, end);

        return orders.stream()
                .map(order -> {
                    CustomerSummary customer = customerLookup.getCustomerSummary(order.getCustomerId());
                    return new OrderWithCustomerView(order, customer);
                })
                .toList();

    }

    @Override
    public Order confirmPayment(UUID id) {
        return unitOfWork.execute(()->{
            Order order = getOrder(id);
            order.confirmPayment();
            Order orderUpdated = orderRepository.save(order);
            orderEventPublisher.publishOrderStatusChange(orderUpdated);
            return orderUpdated;
        });
    }

    @Override
    public Order failPayment(UUID id) {
        return unitOfWork.execute(()->{
            Order order = getOrder(id);
            order.failPayment();
            Order orderUpdated = orderRepository.save(order);
            orderEventPublisher.publishOrderStatusChange(orderUpdated);
            return orderUpdated;
        });
    }

    @Override
    public Order accept(UUID id) {
        return unitOfWork.execute(()->{
            Order order = getOrder(id);
            order.accept();
            Order orderUpdated = orderRepository.save(order);
            orderEventPublisher.publishOrderStatusChange(orderUpdated);
            return orderUpdated;
        });
    }

    @Override
    public Order markReadyForPickup(UUID id) {
        return unitOfWork.execute(()->{
            Order order = getOrder(id);
            order.markReadyForPickup();
            return orderRepository.save(order);
        });
    }

}
