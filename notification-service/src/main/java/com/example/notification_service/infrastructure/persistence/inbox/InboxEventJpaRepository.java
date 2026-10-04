package com.example.notification_service.infrastructure.persistence.inbox;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InboxEventJpaRepository  extends JpaRepository<InboxEventJpaEntity, UUID> {
    boolean existsByEventTypeAndAggregateIdAndRecipientId(String eventType, String aggregateId, String recipientId);
}