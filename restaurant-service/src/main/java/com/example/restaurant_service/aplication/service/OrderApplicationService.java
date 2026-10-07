package com.example.restaurant_service.aplication.service;

import com.example.restaurant_service.aplication.exception.RestaurantNotFoundException;
import com.example.restaurant_service.aplication.port.in.AcceptOrderUseCase;
import com.example.restaurant_service.aplication.port.in.MarkOrderReadyForPickupUseCase;
import com.example.restaurant_service.aplication.port.out.EventPublisherPort;
import com.example.restaurant_service.aplication.port.out.RestaurantRepositoryPort;
import com.example.restaurant_service.aplication.port.out.UnitOfWork;
import com.example.restaurant_service.domain.exception.InvalidRestaurantException;
import com.example.restaurant_service.domain.model.restaurant.Restaurant;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;
import java.util.UUID;

@RequiredArgsConstructor
public class OrderApplicationService implements AcceptOrderUseCase, MarkOrderReadyForPickupUseCase {

    private final RestaurantRepositoryPort restaurantRepository;
    private final EventPublisherPort eventPublisher;
    private final UnitOfWork unitOfWork;

    @Override
    public void accept(UUID restaurantId, UUID orderId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(()-> new RestaurantNotFoundException(restaurantId));

//        if (!restaurant.canAcceptOrders(LocalTime.now())) {
//            throw new InvalidRestaurantException("El restaurante no puede aceptar pedidos en este momento");
//        }

        unitOfWork.execute(() -> {
            eventPublisher.publishRestaurantAccepted(restaurantId,orderId);
            return null;
        });
    }

    @Override
    public void markReadyForPickup(UUID restaurantId,UUID orderId){
        /*Aplicar reglas de negocio*/
        unitOfWork.execute(() -> {
            eventPublisher.publishMarkReadyForPickup(restaurantId,orderId);
            return null;
        });
    }
}
