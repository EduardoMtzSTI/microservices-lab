package com.example.notification_service.infrastructure.sse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class SseConnectionRegistry {

    private final Map<String, SseEmitter> connections = new ConcurrentHashMap<>();

    public SseEmitter register(String recipientId) {
        SseEmitter emitter = new SseEmitter(Duration.ofMinutes(30).toMillis());
        connections.put(recipientId, emitter);
        emitter.onCompletion(() -> connections.remove(recipientId));
        emitter.onTimeout(() -> connections.remove(recipientId));
        emitter.onError(e -> connections.remove(recipientId));

        try {
            // Fuerza a Tomcat a comprometer y enviar los headers + este primer
            // byte de inmediato, en vez de dejarlos retenidos en el buffer
            // esperando datos reales que quizas tarden minutos en llegar.
            emitter.send(SseEmitter.event().name("connected").data("conectado"));
        } catch (IOException e) {
            connections.remove(recipientId);
        }

        return emitter;
    }

    public void pushTo(SsePayload payload) {
        SseEmitter emitter = connections.get(payload.recipientId());
        if (emitter == null) {
            return;   // esta instancia no tiene esa conexión — normal, otra instancia puede tenerla
        }
        try {
            emitter.send(SseEmitter.event().name("notification").data(payload));
            log.info("[SSE] emited: {}",payload.recipientId());
        } catch (IOException e) {
            connections.remove(payload.recipientId());
        }
    }

}
