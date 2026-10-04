package com.deliveryapp.orderservice.infrastructure.messaging.outbox;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Proceso en segundo plano (poller) que es el único componente de todo el
 * sistema que realmente le habla a Kafka. Cada cierto tiempo:
 *   1. Lee de la tabla outbox los eventos con publishedAt = null
 *   2. Los envía a Kafka de forma síncrona (get() con timeout)
 *   3. Si el envío fue exitoso, marca publishedAt
 *   4. Si falló, NO lo marca — el próximo ciclo lo vuelve a intentar
 *
 * Esto da garantías "at-least-once": un evento puede llegar a publicarse más
 * de una vez si el broker confirma pero la respuesta se pierde en la red,
 * así que los consumidores (payment-service, notification-service, etc.)
 * deben ser idempotentes al procesar order.created. Es el trade-off estándar
 * del patrón outbox frente a "exactly-once", que es mucho más complejo de lograr.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxRelayScheduler {

    private final OutboxEventJpaRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

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
                kafkaTemplate.send(event.getTopic(), event.getMessageKey(), event.getPayload())
                        .get(5, TimeUnit.SECONDS);

                event.setPublishedAt(Instant.now());
                log.info("Evento {} del pedido {} publicado en Kafka desde el outbox",
                        event.getEventType(), event.getAggregateId());

            } catch (Exception e) {
                log.error("No se pudo publicar el evento {} (pedido {}) desde el outbox; se reintentará en el próximo ciclo",
                        event.getId(), event.getAggregateId(), e);
                // No se relanza la excepción: un evento fallido no debe bloquear
                // el procesamiento del resto del lote.
            }
        }
    }
}
