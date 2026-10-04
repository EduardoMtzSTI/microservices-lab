package com.deliveryapp.orderservice.domain.model;

import com.deliveryapp.orderservice.domain.exception.InvalidOrderException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Aggregate root del dominio "Order". No tiene ni una sola anotación de Spring,
 * JPA o validación de framework: es una clase Java plana que encapsula las
 * reglas de negocio del pedido.
 *
 * El constructor es privado a propósito: la única forma de crear un pedido
 * válido es a través del método de fábrica {@link #create}, que garantiza
 * que nunca exista un Order en un estado inconsistente (sin items, con total
 * mal calculado, etc). Esto es lo que en DDD se llama "proteger los invariantes
 * del agregado".
 */
public class Order {

    private final UUID id;
    private final String customerId;
    private final String restaurantId;
    private final List<OrderItem> items;
    private final Money totalAmount;
    private OrderStatus status;
    private final Instant createdAt;
    private Instant updatedAt;
    private final String idempotencyKey;

    private Order(UUID id, String customerId, String restaurantId, List<OrderItem> items,
                  Money totalAmount, OrderStatus status, Instant createdAt, Instant updatedAt,
                  String idempotencyKey) {
        this.id = id;
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.items = items;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.idempotencyKey = idempotencyKey;
    }

    /**
     * Método de fábrica: único punto de entrada para crear un pedido nuevo.
     * Aquí viven las reglas de negocio de creación (debe tener al menos un item,
     * el total se calcula sumando los subtotales, nunca se recibe desde afuera).
     */
    public static Order create(String customerId, String restaurantId, List<OrderItem> items, String idempotencyKey) {
        if (customerId == null || customerId.isBlank()) {
            throw new InvalidOrderException("customerId es obligatorio");
        }
        if (restaurantId == null || restaurantId.isBlank()) {
            throw new InvalidOrderException("restaurantId es obligatorio");
        }
        if (items == null || items.isEmpty()) {
            throw new InvalidOrderException("El pedido debe tener al menos un item");
        }

        Money total = items.stream()
                .map(OrderItem::subtotal)
                .reduce(Money.zero(), Money::plus);

        Instant now = Instant.now();
        return new Order(UUID.randomUUID(), customerId, restaurantId, items, total,
                OrderStatus.CREATED, now, now, idempotencyKey);
    }

    /**
     * Reconstruye un Order ya existente (usado por el adaptador de persistencia
     * al leer desde la base de datos). No recalcula nada: confía en los datos
     * ya persistidos.
     */
    public static Order reconstitute(UUID id, String customerId, String restaurantId, List<OrderItem> items,
                                      Money totalAmount, OrderStatus status, Instant createdAt, Instant updatedAt,
                                      String idempotencyKey) {
        return new Order(id, customerId, restaurantId, items, totalAmount, status, createdAt, updatedAt, idempotencyKey);
    }

    /**
     * Cambia el estado del pedido. Se deja como método explícito (en vez de un
     * setter genérico) para que, en el futuro, aquí puedan agregarse las reglas
     * de transición válidas de la saga (ej. no se puede pasar de CANCELLED a
     * DELIVERED). Por ahora, order-service solo usa CREATED; este método queda
     * listo para cuando se consuman eventos de otros servicios.
     */
    public void changeStatus(OrderStatus newStatus) {
        this.status = newStatus;
        this.updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public Money getTotalAmount() {
        return totalAmount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }
}
