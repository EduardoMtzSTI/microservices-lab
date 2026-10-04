package com.example.notification_service.infrastructure.config;

import com.example.notification_service.infrastructure.sse.RedisSseMessageListener;
import com.example.notification_service.infrastructure.sse.SseConnectionRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import tools.jackson.databind.ObjectMapper;


import static com.example.notification_service.infrastructure.sse.RedisSseBroadcastAdapter.CHANNEL;

@Configuration
public class RedisConfig {

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory connectionFactory,
            SseConnectionRegistry registry,
            ObjectMapper objectMapper) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(new RedisSseMessageListener(registry, objectMapper), new ChannelTopic(CHANNEL));
        return container;
    }

}
