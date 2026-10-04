package com.example.order_service.domain.model;

import com.example.order_service.domain.exception.InvalidOrderException;
import com.example.order_service.domain.validation.OrderValidator;
import com.example.order_service.domain.validation.ValidationResult;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
public class Order {

    private final UUID id;
    private final UUID customerId;
    private final UUID restaurantId;
    private final List<OrderItem> items;
    private final Money totalAmount;
    private OrderStatus status;
    private final Instant createdAt;
    private Instant updatedAt;
    private final String idempotencyKey;

    private Order(OrderSnapshot orderSnapshot){
        this.id=orderSnapshot.id();
        this.customerId=orderSnapshot.customerId();
        this.restaurantId=orderSnapshot.restaurantId();
        this.items=orderSnapshot.items();
        this.totalAmount=orderSnapshot.totalAmount();
        this.status=orderSnapshot.status();
        this.createdAt=orderSnapshot.createdAt();
        this.updatedAt=orderSnapshot.updatedAt();
        this.idempotencyKey=orderSnapshot.idempotencyKey();
    }

    public static Order create(NewOrderData data) {

        ValidationResult validation = OrderValidator.all(
                OrderValidator.hasCustomerId(),
                OrderValidator.hasRestaurantId(),
                OrderValidator.hasAtLeastOneItem()
        ).apply(data);

        if(!validation.isValid())
            throw new InvalidOrderException(String.join("; ",validation.errors()));

        Money total = data.items().stream()
                .map(OrderItem::subtotal)
                .reduce(Money::plus)
                .orElseThrow(() -> new IllegalStateException("No debería ocurrir: ya se validó que hay items"));

        Instant now = Instant.now();

        OrderSnapshot os = OrderSnapshot.builder()
                .id(UUID.randomUUID())
                .customerId(data.customerId())
                .restaurantId(data.restaurantId())
                .items(data.items())
                .totalAmount(total)
                .status(OrderStatus.CREATED)
                .createdAt(now)
                .updatedAt(now)
                .idempotencyKey(data.idempotencyKey())
                .build();

        return new Order(os);
    }

    public static Order reconstitute(OrderSnapshot orderSnapshot){
        return new Order(orderSnapshot);
    }

    public void confirmPayment(){
        requireStatus(OrderStatus.CREATED,"confirmar el pago");
        this.status=OrderStatus.PAYMENT_CONFIRMED;
        this.updatedAt=Instant.now();
    }

    public void failPayment(){
        requireStatus(OrderStatus.PAYMENT_FAILED,"marcar el pago como fallido");
        this.status=OrderStatus.PAYMENT_FAILED;
        this.updatedAt=Instant.now();
    }

    public void accept(){
        requireStatus(OrderStatus.PAYMENT_CONFIRMED,"aceptar");
        this.status=OrderStatus.RESTAURANT_ACCEPTED;
        this.updatedAt=Instant.now();
    }

    public void markReadyForPickup(){
        requireStatus(OrderStatus.RESTAURANT_ACCEPTED,"marcar para recoger");
        this.status=OrderStatus.READY_FOR_PICKUP;
        this.updatedAt=Instant.now();
    }

    private void requireStatus(OrderStatus expected, String action){
        if(this.status!=expected)
            throw new InvalidOrderException(
                    "Solo se puede %s cuando el pedido esta en %s, estado actual: %s"
                    .formatted(action, expected, this.status)
            );
    }

}
