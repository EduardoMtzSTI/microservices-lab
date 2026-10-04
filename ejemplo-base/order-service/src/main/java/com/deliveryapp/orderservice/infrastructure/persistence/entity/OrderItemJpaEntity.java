package com.deliveryapp.orderservice.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad de persistencia (JPA). A propósito NO es la misma clase que
 * domain.model.OrderItem: esta lleva anotaciones de Hibernate, la otra es
 * un objeto de negocio puro. Mantenerlas separadas permite, por ejemplo,
 * cambiar el mapeo de columnas sin tocar ni una línea de lógica de negocio.
 */
@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 19, scale = 2)
    private java.math.BigDecimal unitPrice;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderJpaEntity order;
}
