package com.example.restaurant_service.infrastructure.persistence.mapper;

import com.example.restaurant_service.domain.model.restaurant.Restaurant;
import com.example.restaurant_service.domain.model.restaurant.RestaurantSnapshot;
import com.example.restaurant_service.infrastructure.persistence.entity.RestaurantJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestaurantPersistenceMapper {

    RestaurantSnapshot toSnapshot(RestaurantJpaEntity entity);

    RestaurantJpaEntity toJpaEntity(Restaurant restaurant);

    default Restaurant toDomain(RestaurantJpaEntity entity) {
        return Restaurant.reconstitute(toSnapshot(entity));
    }
}