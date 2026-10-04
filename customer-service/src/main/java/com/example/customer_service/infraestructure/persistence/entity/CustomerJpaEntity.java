package com.example.customer_service.infraestructure.persistence.entity;

import com.example.customer_service.domain.model.CustomerStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String number;

    @Column(nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustomerStatus status;

    @Column(nullable = false)
    private Instant createdAt;

    private Instant updatedAt;

}
