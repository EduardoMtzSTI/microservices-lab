package com.example.notification_service.infrastructure.persistence.inbox;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "inbox_events",
        uniqueConstraints = @UniqueConstraint(columnNames = {"eventType", "aggregateId", "recipientId"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InboxEventJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false)
    private String aggregateId;

    @Column(nullable = false)
    private String recipientId;

    @Column(nullable = false)
    private Instant processedAt;

}
