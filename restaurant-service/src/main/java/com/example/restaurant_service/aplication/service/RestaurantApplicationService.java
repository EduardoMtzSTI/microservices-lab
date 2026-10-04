package com.example.restaurant_service.aplication.service;

import com.example.restaurant_service.aplication.exception.RestaurantNotFoundException;
import com.example.restaurant_service.aplication.port.in.CreateRestaurantCommand;
import com.example.restaurant_service.aplication.port.in.CreateRestaurantUseCase;
import com.example.restaurant_service.aplication.port.in.RestaurantQueries;
import com.example.restaurant_service.aplication.port.out.RestaurantRepositoryPort;
import com.example.restaurant_service.domain.model.restaurant.NewRestaurantData;
import com.example.restaurant_service.domain.model.restaurant.Restaurant;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class RestaurantApplicationService implements CreateRestaurantUseCase, RestaurantQueries {

    private final RestaurantRepositoryPort restaurantRepository;

    @Override
    public Restaurant createRestaurant(CreateRestaurantCommand command) {
        NewRestaurantData data = NewRestaurantData.builder()
                .name(command.name())
                .address(command.address())
                .phone(command.phone())
                .openingTime(command.openingTime())
                .closingTime(command.closingTime())
                .build();
        Restaurant restaurant = Restaurant.create(data);
        Restaurant restaurantSaved = restaurantRepository.save(restaurant);
        return restaurantSaved;
    }

    @Override
    public List<Restaurant> listRestaurants() {
        return restaurantRepository.findAll();
    }

    @Override
    public Restaurant getRestaurant(UUID id) {
        return restaurantRepository.findById(id)
                .orElseThrow(()-> new RestaurantNotFoundException(id));
    }
}
