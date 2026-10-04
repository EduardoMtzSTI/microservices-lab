package com.example.payment_service.infrastructure.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class PaymentTopicConfig {
    @Bean
    public NewTopic paymentConfirmedTopic(@Value("${app.kafka.topic.payment-confirmed}") String topic) {
        return TopicBuilder.name(topic).partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic paymentFailedTopic(@Value("${app.kafka.topic.payment-failed}") String topic) {
        return TopicBuilder.name(topic).partitions(3).replicas(1).build();
    }
}
