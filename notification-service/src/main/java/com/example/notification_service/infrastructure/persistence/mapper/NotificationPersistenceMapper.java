package com.example.notification_service.infrastructure.persistence.mapper;

import com.example.notification_service.domain.model.Notification;
import com.example.notification_service.domain.model.NotificationSnapshot;
import com.example.notification_service.infrastructure.persistence.entity.NotificationJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationPersistenceMapper {

    NotificationSnapshot toSnapshot(NotificationJpaEntity entity);

    NotificationJpaEntity toJpaEntity(Notification entity);

    default Notification toDomain(NotificationJpaEntity entity){
        return Notification.reconstitute(toSnapshot(entity));
    }

}
