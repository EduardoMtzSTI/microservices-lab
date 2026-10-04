package com.example.order_service.infraestructure.config;

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
