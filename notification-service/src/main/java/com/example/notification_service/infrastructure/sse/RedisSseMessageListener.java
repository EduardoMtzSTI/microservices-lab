package com.example.notification_service.infrastructure.sse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedisSseMessageListener implements MessageListener {

    private final SseConnectionRegistry registry;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            SsePayload payload = objectMapper.readValue(message.getBody(), SsePayload.class);
            registry.pushTo(payload);
        } catch (Exception e) {
            log.warn("Mensaje SSE mal formado en el canal de Redis, se ignora", e);
        }
    }
}
