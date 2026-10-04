package com.example.restaurant_service.infraestructure.persistence.repository;

import com.example.restaurant_service.infraestructure.persistence.entity.RestaurantJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataRestaurantJpaRepository extends JpaRepository<RestaurantJpaEntity, UUID> {
}
