package com.example.notification_service.infrastructure.config;

import com.example.notification_service.aplication.port.out.*;
import com.example.notification_service.aplication.service.NotificationApplicationService;
import com.example.notification_service.domain.model.NotificationChannel;
import com.example.notification_service.infrastructure.messaging.consumer.OrderCreatedEventListener;
import com.example.notification_service.infrastructure.messaging.consumer.PaymentConfirmedEventListener;
import com.example.notification_service.infrastructure.messaging.consumer.PaymentFailedEventListener;
import com.example.notification_service.infrastructure.persistence.SpringUnitOfWork;
import com.example.notification_service.infrastructure.persistence.adapter.NotificationRepositoryAdapter;
import com.example.notification_service.infrastructure.persistence.inbox.InboxEventJpaRepository;
import com.example.notification_service.infrastructure.persistence.inbox.JpaNotificationInboxAdapter;
import com.example.notification_service.infrastructure.persistence.mapper.NotificationPersistenceMapper;
import com.example.notification_service.infrastructure.persistence.repository.NotificationEntityJpaRepository;
import com.example.notification_service.infrastructure.sender.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@Configuration
public class NotificationBeansConfig {

    @Bean
    public UnitOfWork unitOfWork(PlatformTransactionManager transactionManager){
        return new SpringUnitOfWork(transactionManager);
    }

    @Bean
    public NotificationApplicationService notificationApplicationService(
              NotificationInboxPort notificationInboxPort,
              NotificationRepositoryPort notificationRepositoryPort,
              NotificationSenderPort notificationSenderPort,
              UnitOfWork unitOfWork
      ){
          return new NotificationApplicationService(
                  notificationInboxPort,
                  notificationRepositoryPort,
                  notificationSenderPort,
                  unitOfWork
          );
      }

    @Bean
    public OrderCreatedEventListener orderCreatedEventListener(
            NotificationApplicationService notificationApplicationService,
            ObjectMapper objectMapper) {
        return new OrderCreatedEventListener(notificationApplicationService, objectMapper);
    }

    @Bean
    public PaymentConfirmedEventListener paymentConfirmedEventListener(
            NotificationApplicationService notificationApplicationService,
            ObjectMapper objectMapper) {
        return new PaymentConfirmedEventListener(notificationApplicationService, objectMapper);
    }

    @Bean
    public PaymentFailedEventListener paymentFailedEventListener(
            NotificationApplicationService notificationApplicationService,
            ObjectMapper objectMapper) {
        return new PaymentFailedEventListener(notificationApplicationService, objectMapper);
    }

    @Bean
    public NotificationSenderPort notificationSenderPort(SseBroadcastPort sseBroadcastPort) {
        Map<NotificationChannel, NotificationSenderPort> senders = Map.of(
                NotificationChannel.EMAIL, new EmailNotificationSenderAdapter(),
                NotificationChannel.PUSH, new PushNotificationSenderAdapter(),
                NotificationChannel.SMS, new SmsNotificationSenderAdapter(),
                NotificationChannel.SSE, new SseNotificationSenderAdapter(sseBroadcastPort)
        );
        return new CompositeNotificationSenderAdapter(senders);
    }

    @Bean
    public NotificationRepositoryPort notificationRepositoryPort(
            NotificationEntityJpaRepository notificationEntityJpaRepository,
            NotificationPersistenceMapper notificationPersistenceMapper
    ){
        return new NotificationRepositoryAdapter(notificationEntityJpaRepository,notificationPersistenceMapper);
    }

    @Bean
    public NotificationInboxPort notificationInboxPort(InboxEventJpaRepository inboxEventJpaRepository){
        return new JpaNotificationInboxAdapter(inboxEventJpaRepository);
    }

}
