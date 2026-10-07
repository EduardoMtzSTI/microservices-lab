package com.example.restaurant_service.infrastructure.config;

import com.example.restaurant_service.aplication.port.out.EventPublisherPort;
import com.example.restaurant_service.aplication.port.out.ProductRepositoryPort;
import com.example.restaurant_service.aplication.port.out.RestaurantRepositoryPort;
import com.example.restaurant_service.aplication.port.out.UnitOfWork;
import com.example.restaurant_service.aplication.service.OrderApplicationService;
import com.example.restaurant_service.aplication.service.ProductApplicationService;
import com.example.restaurant_service.aplication.service.RestaurantApplicationService;
import com.example.restaurant_service.infrastructure.persistence.SpringUnitOfWork;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ApplicationBeansConfig {

    @Bean
    public UnitOfWork unitOfWork(PlatformTransactionManager transactionManager){
        return new SpringUnitOfWork(transactionManager);
    }

    @Bean
    public ProductApplicationService productApplicationService(
            ProductRepositoryPort orderRepository,
            RestaurantRepositoryPort restaurantRepository
    ) {
        return new ProductApplicationService(orderRepository,restaurantRepository);
    }

    @Bean
    public RestaurantApplicationService restaurantApplicationService(
            RestaurantRepositoryPort restaurantRepository
    ) {
        return new RestaurantApplicationService(restaurantRepository);
    }

    @Bean
    public OrderApplicationService orderApplicationService(
            RestaurantRepositoryPort restaurantRepository,
            EventPublisherPort eventPublisherPort,
            UnitOfWork unitOfWork
    ){
        return new OrderApplicationService(restaurantRepository,eventPublisherPort,unitOfWork);
    }
}