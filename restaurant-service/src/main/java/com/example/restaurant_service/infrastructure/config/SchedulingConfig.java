package com.example.restaurant_service.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Habilita la ejecución de métodos anotados con @Scheduled, requerido por
 * OutboxRelayScheduler para relanzar periódicamente los eventos pendientes.
 */
@Configuration
@EnableScheduling
public class SchedulingConfig {
}
