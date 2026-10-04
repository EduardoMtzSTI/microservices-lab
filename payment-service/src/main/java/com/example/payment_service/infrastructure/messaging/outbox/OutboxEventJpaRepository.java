package com.example.payment_service.infrastructure.messaging.outbox;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OutboxEventJpaRepository extends JpaRepository<OutboxEventJpaEntity, UUID> {
    List<OutboxEventJpaEntity> findTop50ByPublishedAtIsNullOrderByCreatedAtAsc();
}