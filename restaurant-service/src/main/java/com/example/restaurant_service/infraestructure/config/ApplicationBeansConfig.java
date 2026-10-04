package com.example.restaurant_service.infraestructure.config;

import com.example.restaurant_service.aplication.port.out.ProductRepositoryPort;
import com.example.restaurant_service.aplication.port.out.RestaurantRepositoryPort;
import com.example.restaurant_service.aplication.service.ProductApplicationService;
import com.example.restaurant_service.aplication.service.RestaurantApplicationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationBeansConfig {

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
}