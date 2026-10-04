package com.example.payment_service.infrastructure.config;

import com.example.payment_service.aplication.port.out.PaymentEventPublisherPort;
import com.example.payment_service.aplication.port.out.PaymentGatewayPort;
import com.example.payment_service.aplication.port.out.PaymentRepositoryPort;
import com.example.payment_service.aplication.port.out.UnitOfWork;
import com.example.payment_service.aplication.service.PaymentApplicationService;
import com.example.payment_service.infrastructure.gateway.SimulatedPaymentGatewayAdapter;
import com.example.payment_service.infrastructure.messaging.consumer.OrderCreatedEventListener;
import com.example.payment_service.infrastructure.messaging.outbox.OutboxEventJpaRepository;
import com.example.payment_service.infrastructure.messaging.outbox.OutboxPaymentEventPublisherAdapter;
import com.example.payment_service.infrastructure.persistence.SpringUnitOfWork;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class ApplicationBeansConfig {

    @Bean
    public UnitOfWork unitOfWork(PlatformTransactionManager transactionManager) {
        return new SpringUnitOfWork(transactionManager);
    }

    @Bean
    public PaymentGatewayPort paymentGatewayPort() {
        return new SimulatedPaymentGatewayAdapter();
    }

    @Bean
    public PaymentEventPublisherPort paymentEventPublisherPort(
            OutboxEventJpaRepository outboxRepository,
            ObjectMapper objectMapper,
            @Value("${app.kafka.topic.payment-confirmed}") String paymentConfirmedTopic,
            @Value("${app.kafka.topic.payment-failed}") String paymentFailedTopic) {
        return new OutboxPaymentEventPublisherAdapter(
                outboxRepository, objectMapper, paymentConfirmedTopic, paymentFailedTopic);
    }

    @Bean
    public PaymentApplicationService paymentApplicationService(
            PaymentRepositoryPort paymentRepository,
            PaymentEventPublisherPort paymentEventPublisher,
            PaymentGatewayPort paymentGateway,
            UnitOfWork unitOfWork) {
        return new PaymentApplicationService(paymentRepository, paymentEventPublisher, paymentGateway, unitOfWork);
    }

    @Bean
    public OrderCreatedEventListener orderCreatedEventListener(
            PaymentApplicationService paymentApplicationService,
            ObjectMapper objectMapper) {
        return new OrderCreatedEventListener(paymentApplicationService, objectMapper);
    }
}