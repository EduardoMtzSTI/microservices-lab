package com.deliveryapp.orderservice.infrastructure.messaging.outbox;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OutboxEventJpaRepository extends JpaRepository<OutboxEventJpaEntity, UUID> {

    // Trae hasta 50 eventos pendientes por ciclo, en orden de creación, para no
    // saturar el broker si se acumula un backlog grande tras una caída de Kafka.
    List<OutboxEventJpaEntity> findTop50ByPublishedAtIsNullOrderByCreatedAtAsc();
}
