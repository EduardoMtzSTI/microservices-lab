package com.example.restaurant_service.domain.model.restaurant;

import com.example.restaurant_service.domain.exception.InvalidRestaurantException;
import com.example.restaurant_service.domain.validation.RestaurantValidator;
import com.example.restaurant_service.domain.validation.ValidationResult;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalTime;
import java.util.UUID;

@Getter
public class Restaurant {

    private final UUID id;
    private String name;
    private String address;
    private String phone;
    private RestaurantStatus status;
    private boolean open;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private final Instant createdAt;
    private Instant updatedAt;

    private Restaurant(RestaurantSnapshot snapshot) {
        this.id = snapshot.id();
        this.name = snapshot.name();
        this.address = snapshot.address();
        this.phone = snapshot.phone();
        this.status = snapshot.status();
        this.open = snapshot.open();
        this.openingTime = snapshot.openingTime();
        this.closingTime = snapshot.closingTime();
        this.createdAt = snapshot.createdAt();
        this.updatedAt = snapshot.updatedAt();
    }

    public static Restaurant create(NewRestaurantData data) {

        ValidationResult validation = RestaurantValidator.all(
                RestaurantValidator.hasName(),
                RestaurantValidator.hasAddress(),
                RestaurantValidator.hasPhoneValid(),
                RestaurantValidator.hasValidServiceHours()
        ).apply(data);

        if (!validation.isValid()) {
            throw new InvalidRestaurantException(String.join("; ", validation.errors()));
        }

        Instant now = Instant.now();
        return new Restaurant(RestaurantSnapshot.builder()
                .id(UUID.randomUUID())
                .name(data.name())
                .address(data.address())
                .phone(data.phone())
                .openingTime(data.openingTime())
                .closingTime(data.closingTime())
                .status(RestaurantStatus.ACTIVE)
                .open(true)
                .createdAt(now)
                .updatedAt(now)
                .build());
    }

    public static Restaurant reconstitute(RestaurantSnapshot snapshot){
        return new Restaurant(snapshot);
    }

    public boolean canAcceptOrders(LocalTime now) {
        boolean withinServiceHours = !now.isBefore(openingTime) && now.isBefore(closingTime);
        return this.status == RestaurantStatus.ACTIVE && this.open && withinServiceHours;
    }

}
