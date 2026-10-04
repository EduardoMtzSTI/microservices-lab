package com.deliveryapp.orderservice.domain.model;

/**
 * Objeto de dominio (no JPA) que representa un ítem dentro de un pedido.
 * Valida sus propias invariantes en el constructor: un OrderItem inválido
 * simplemente no puede existir en memoria, sin depender de anotaciones
 * de validación de Spring.
 */
public class OrderItem {

    private final String productName;
    private final int quantity;
    private final Money unitPrice;

    public OrderItem(String productName, int quantity, Money unitPrice) {
        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero, recibido: " + quantity);
        }
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public Money subtotal() {
        return unitPrice.multiply(quantity);
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public Money getUnitPrice() {
        return unitPrice;
    }
}
