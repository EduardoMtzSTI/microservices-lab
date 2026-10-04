package com.example.notification_service.infrastructure.sse;

import com.example.notification_service.aplication.port.out.BroadcastNotificationCommand;
import com.example.notification_service.aplication.port.out.SseBroadcastPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedisSseBroadcastAdapter implements SseBroadcastPort{

    public static final String CHANNEL = "sse-notifications";
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void broadcast(BroadcastNotificationCommand command) {
        try {
            String payload = objectMapper.writeValueAsString(command);
            redisTemplate.convertAndSend(CHANNEL, payload);
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo publicar el evento SSE en Redis", e);
        }
    }

}
