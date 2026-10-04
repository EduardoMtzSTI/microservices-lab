package com.example.payment_service.infrastructure.messaging.outbox;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payment_outbox_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutboxEventJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String aggregateType;   // "Payment"

    @Column(nullable = false)
    private String aggregateId;     // el orderId, como String (no el paymentId — así el

    @Column(nullable = false)
    private String eventType;       // "payment.confirmed" o "payment.failed"

    @Column(nullable = false)
    private String topic;           // tópico destino en Kafka

    @Column(nullable = false)
    private String messageKey;      // el orderId como key, mantiene el orden de eventos por pedido

    @Lob
    @Column(nullable = false)
    private String payload;         // el evento ya serializado a JSON

    @Column(nullable = false)
    private Instant createdAt;

    private Instant publishedAt;
}