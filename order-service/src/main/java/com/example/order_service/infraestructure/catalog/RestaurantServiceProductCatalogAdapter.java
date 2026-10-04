package com.example.order_service.infraestructure.catalog;

import com.example.order_service.aplication.port.out.ProductCatalogPort;
import com.example.order_service.aplication.port.out.ProductPrice;
import com.example.order_service.domain.exception.InvalidOrderException;
import com.example.order_service.domain.model.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Currency;
import java.util.UUID;

@RequiredArgsConstructor
public class RestaurantServiceProductCatalogAdapter implements ProductCatalogPort {

    private final RestClient restClient;

    @Override
    public ProductPrice getCurrentPrice(UUID productId) {
        try{

            RestaurantProductApiResponse response = restClient.get()
                    .uri("/api/products/{id}", productId)
                    .retrieve()
                    .body(RestaurantProductApiResponse.class);

            assert response != null;
            Currency currency = Currency.getInstance(response.currency());
            Money money = Money.of(response.price(),currency);

            return ProductPrice.builder()
                    .productId(response.id())
                    .name(response.name())
                    .price(money)
                    .build();

        }catch(HttpClientErrorException.NotFound e){
            throw new InvalidOrderException("Producto no encontrado: " + productId);
        } catch (RestClientException e) {
            throw new IllegalStateException("No se pudo consultar restaurant-service para el producto " + productId, e);
        }
    }
}
