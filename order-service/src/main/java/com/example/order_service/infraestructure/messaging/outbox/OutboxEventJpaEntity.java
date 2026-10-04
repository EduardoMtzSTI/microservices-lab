package com.example.order_service.infraestructure.messaging.outbox;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "order_outbox_events")
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

    private Instant publishedAt;
}

