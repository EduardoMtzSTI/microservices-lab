package com.example.notification_service.aplication.service;

import com.example.notification_service.aplication.port.in.SendNotificationCommand;
import com.example.notification_service.aplication.port.in.SendNotificationUseCase;
import com.example.notification_service.aplication.port.out.NotificationInboxPort;
import com.example.notification_service.aplication.port.out.NotificationRepositoryPort;
import com.example.notification_service.aplication.port.out.NotificationSenderPort;
import com.example.notification_service.aplication.port.out.UnitOfWork;
import com.example.notification_service.domain.model.NewNotificationData;
import com.example.notification_service.domain.model.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
public class NotificationApplicationService implements SendNotificationUseCase {

    private final NotificationInboxPort notificationInbox;
    private final NotificationRepositoryPort notificationRepository;
    private final NotificationSenderPort notificationSender;
    private final UnitOfWork unitOfWork;

    @Override
    public void send(SendNotificationCommand command) {

        boolean firstTime = notificationInbox.tryMarkAsProcessed(
                command.eventType(),
                command.aggregateId(),
                command.recipientId()
        );

        if(!firstTime){
            log.info("Evento {} del agregado {} ya fue procesado, se ignora",
                    command.eventType(), command.aggregateId());
            return;
        }

        NewNotificationData data = NewNotificationData.builder()
                .recipientId(command.recipientId())
                .eventType(command.eventType())
                .aggregateId(command.aggregateId())
                .channel(command.channel())
                .message(command.message())
                .build();

        Notification notification = Notification.create(data);

        try {
            notificationSender.send(notification);
            notification.markSent();
        }catch (RuntimeException e){
            log.error("No se pudo enviar la notificación {} por {}", command.eventType(), command.channel(), e);
            String message = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
            notification.markFailed(message);
        }

        unitOfWork.execute(()->notificationRepository.save(notification));
    }

}
