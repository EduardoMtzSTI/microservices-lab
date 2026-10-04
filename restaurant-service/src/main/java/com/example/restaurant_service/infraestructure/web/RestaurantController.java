package com.example.restaurant_service.infraestructure.web;

import com.example.restaurant_service.aplication.port.in.CreateRestaurantCommand;
import com.example.restaurant_service.aplication.port.in.CreateRestaurantUseCase;
import com.example.restaurant_service.aplication.port.in.RestaurantQueries;
import com.example.restaurant_service.domain.model.restaurant.Restaurant;
import com.example.restaurant_service.infraestructure.web.dto.CreateRestaurantRequest;
import com.example.restaurant_service.infraestructure.web.dto.RestaurantResponse;
import com.example.restaurant_service.infraestructure.web.mapper.RestaurantWebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final CreateRestaurantUseCase createRestaurant;
    private final RestaurantQueries restaurantQueries;
    private final RestaurantWebMapper mapper;

    @PostMapping
    public ResponseEntity<RestaurantResponse> create(
            @Valid @RequestBody CreateRestaurantRequest request
    ){
        CreateRestaurantCommand command = mapper.toCommand(request);
        Restaurant restaurantCreated = createRestaurant.createRestaurant(command);
        RestaurantResponse restaurantResponse = mapper.toResponse(restaurantCreated);

        return ResponseEntity
                .created(URI.create("/api/restaurants/"+restaurantResponse.id()))
                .body(restaurantResponse);
    }

    @GetMapping("/{idRestaurant}")
    public ResponseEntity<RestaurantResponse> getRestaurant(
            @PathVariable UUID idRestaurant
    ){
        Restaurant restaurant = restaurantQueries.getRestaurant(idRestaurant);
        RestaurantResponse restaurantResponse = mapper.toResponse(restaurant);
        return  ResponseEntity.ok(restaurantResponse);
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResponse>> getRestaurant(){
        List<Restaurant> restaurants = restaurantQueries.listRestaurants();
        List<RestaurantResponse> restaurantsResponse = restaurants.stream()
                .map(mapper::toResponse)
                .toList();
        return  ResponseEntity.ok(restaurantsResponse);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.status(HttpStatus.OK).body("restaurant-controller está funcionando correctamente");
    }

}
