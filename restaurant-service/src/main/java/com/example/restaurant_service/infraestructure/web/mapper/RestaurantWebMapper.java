package com.example.restaurant_service.infraestructure.web.mapper;

import com.example.restaurant_service.aplication.port.in.CreateRestaurantCommand;
import com.example.restaurant_service.domain.model.restaurant.Restaurant;
import com.example.restaurant_service.infraestructure.web.dto.CreateRestaurantRequest;
import com.example.restaurant_service.infraestructure.web.dto.RestaurantResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestaurantWebMapper {

    CreateRestaurantCommand toCommand(CreateRestaurantRequest request);
    RestaurantResponse toResponse(Restaurant restaurant);

}
