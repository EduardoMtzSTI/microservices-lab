package com.deliveryapp.orderservice.infrastructure.messaging.outbox;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * Tabla técnica del patrón Transactional Outbox: cada fila es "un evento
 * pendiente de publicar en Kafka". Se guarda en la MISMA base de datos y,
 * por lo tanto, en la MISMA transacción que el pedido — eso es lo que
 * garantiza que nunca se pierda un evento por una caída de Kafka justo
 * después de guardar el pedido.
 */
@Entity
@Table(name = "outbox_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutboxEventJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String aggregateType;   // ej. "Order"

    @Column(nullable = false)
    private String aggregateId;     // el orderId, como String

    @Column(nullable = false)
    private String eventType;       // ej. "order.created"

    @Column(nullable = false)
    private String topic;           // tópico destino en Kafka

    @Column(nullable = false)
    private String messageKey;      // key del mensaje (para mantener orden por pedido)

    @Lob
    @Column(nullable = false)
    private String payload;         // el evento ya serializado a JSON

    @Column(nullable = false)
    private Instant createdAt;

    // null = todavía no se publicó en Kafka; el relay lo procesa y lo marca
    private Instant publishedAt;
}
