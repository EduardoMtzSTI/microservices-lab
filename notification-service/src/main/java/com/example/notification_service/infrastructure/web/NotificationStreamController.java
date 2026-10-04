package com.example.notification_service.infrastructure.web;

import com.example.notification_service.infrastructure.sse.SseConnectionRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationStreamController {

    private final SseConnectionRegistry registry;

    /**
     * El dashboard del restaurante abre esta conexión UNA vez y la deja abierta.
     * No devuelve nada "una sola vez" — SseEmitter mantiene el socket abierto y
     * Spring va empujando eventos por él conforme registry.pushTo(...) los recibe.
     */
    @GetMapping(path = "/stream/{recipientId}", produces = "text/event-stream")
    public SseEmitter stream(@PathVariable String recipientId) {
        return registry.register(recipientId);
    }

}