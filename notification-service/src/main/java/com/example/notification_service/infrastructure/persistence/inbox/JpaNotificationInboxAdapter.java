package com.example.notification_service.infrastructure.persistence.inbox;

import com.example.notification_service.aplication.port.out.NotificationInboxPort;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
public class JpaNotificationInboxAdapter implements NotificationInboxPort {

    private final InboxEventJpaRepository inboxRepository;

    @Override
    public boolean tryMarkAsProcessed(String eventType, String aggregateId, String recipientId) {

        boolean existEvent =inboxRepository.existsByEventTypeAndAggregateIdAndRecipientId(
                eventType,
                aggregateId,
                recipientId);

        if (existEvent) {
            return false;
        }

        try {
            inboxRepository.save(InboxEventJpaEntity.builder()
                    .id(UUID.randomUUID())
                    .eventType(eventType)
                    .aggregateId(aggregateId)
                    .recipientId(recipientId)
                    .processedAt(Instant.now())
                    .build());
            return true;
        } catch (DataIntegrityViolationException div) {
            // Otro hilo/instancia ganó la carrera entre el exists() y el save() -
            // la constraint de BD lo protegió, no es un error real.
            return false;
        }
    }

}
