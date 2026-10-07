package com.example.restaurant_service.infrastructure.web;

import com.example.restaurant_service.aplication.port.in.*;
import com.example.restaurant_service.domain.model.restaurant.Restaurant;
import com.example.restaurant_service.infrastructure.web.dto.CreateRestaurantRequest;
import com.example.restaurant_service.infrastructure.web.dto.RestaurantResponse;
import com.example.restaurant_service.infrastructure.web.mapper.RestaurantWebMapper;
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
    private final AcceptOrderUseCase acceptOrderUseCase;
    private final MarkOrderReadyForPickupUseCase markOrderReadyForPickupUseCase;
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

    @PatchMapping("/{restaurantId}/order/{orderId}/accept")
    public ResponseEntity<Void> acceptOrder(
            @PathVariable UUID restaurantId,
            @PathVariable UUID orderId
    ) {
        acceptOrderUseCase.accept(restaurantId,orderId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{restaurantId}/order/{orderId}/ready")
    public ResponseEntity<Void> markReady(
            @PathVariable UUID restaurantId,
            @PathVariable UUID orderId
    ) {
        markOrderReadyForPickupUseCase.markReadyForPickup(restaurantId,orderId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.status(HttpStatus.OK).body("restaurant-controller está funcionando correctamente");
    }

}
