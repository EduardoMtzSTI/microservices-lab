package com.example.customer_service.infraestructure.config;

import com.example.customer_service.aplication.port.out.CustomerRepositoryPort;
import com.example.customer_service.aplication.service.CustomerApplicationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationBeansConfig {

    @Bean
    public CustomerApplicationService customerService(CustomerRepositoryPort customerRepositoryPort){
        return  new CustomerApplicationService(customerRepositoryPort);
    }
}
