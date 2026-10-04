package com.example.restaurant_service.infraestructure.web.mapper;

import com.example.restaurant_service.aplication.port.in.CreateProductCommand;
import com.example.restaurant_service.aplication.query.ProductWithRestaurantView;
import com.example.restaurant_service.domain.model.product.Product;
import com.example.restaurant_service.domain.model.restaurant.Restaurant;
import com.example.restaurant_service.infraestructure.web.dto.CreateProductRequest;
import com.example.restaurant_service.infraestructure.web.dto.ProductResponse;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class  ProductWebMapper {

    public CreateProductCommand toCommand(CreateProductRequest request,String idempotencyKey){
        UUID restaurantId = UUID.fromString(request.restaurantId());
        return CreateProductCommand.builder()
                .name(request.name())
                .price(request.price())
                .currency(request.currency())
                .description(request.description())
                .category(request.category())
                .restaurantId(restaurantId)
                .idempotencyKey(idempotencyKey)
                .build();
    }

    public ProductResponse toResponse(Product product){
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice().toDecimal())
                .currency(product.getPrice().currency().getCurrencyCode())
                .available(product.isAvaileble())
                .category(product.getCategory())
                .build();
    }

    public ProductResponse toResponse(ProductWithRestaurantView view){
        Product product = view.product();
        Restaurant restaurant = view.restaurant();

        var restaurantResponse = ProductResponse.RestaurantSummary.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .openingTime(restaurant.getOpeningTime())
                .closingTime(restaurant.getClosingTime())
                .open(restaurant.isOpen())
                .build();

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice().toDecimal())
                .currency(product.getPrice().currency().getCurrencyCode())
                .available(product.isAvaileble())
                .category(product.getCategory())
                .restaurant(restaurantResponse)
                .build();
    }

}
