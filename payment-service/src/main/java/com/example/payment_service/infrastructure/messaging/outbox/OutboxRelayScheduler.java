package com.example.payment_service.infrastructure.messaging.outbox;

import com.example.payment_service.aplication.port.out.UnitOfWork;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxRelayScheduler {

    private final OutboxEventJpaRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final UnitOfWork unitOfWork;

    @Scheduled(fixedDelayString = "${app.outbox.relay-interval-ms:2000}")
    @Transactional
    public void relayPendingEvents() {
        List<OutboxEventJpaEntity> pendingEvents = outboxRepository.findTop50ByPublishedAtIsNullOrderByCreatedAtAsc();

        if (pendingEvents.isEmpty()) {
            return;
        }

        log.debug("Relay de outbox: {} evento(s) pendiente(s) por publicar", pendingEvents.size());

        for (OutboxEventJpaEntity event : pendingEvents) {
            try {
                // Llamada de red FUERA de cualquier transacción — igual que la
                kafkaTemplate.send(event.getTopic(), event.getMessageKey(), event.getPayload())
                        .get(5, TimeUnit.SECONDS);

                // Solo el UPDATE es transaccional, y dura milisegundos, no segundos.
                unitOfWork.execute(() -> {
                    event.setPublishedAt(Instant.now());
                    return outboxRepository.save(event);
                });

                log.info("Evento {} del pago {} publicado en Kafka desde el outbox",
                        event.getEventType(), event.getAggregateId());

            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                log.error("Interrumpido al publicar el evento {} (pago {})", event.getId(), event.getAggregateId(), ie);
            } catch (Exception e) {
                log.error("No se pudo publicar el evento {} (pago {}) desde el outbox; se reintentara en el proximo ciclo",
                        event.getId(), event.getAggregateId(), e);
            }
        }

    }
}
