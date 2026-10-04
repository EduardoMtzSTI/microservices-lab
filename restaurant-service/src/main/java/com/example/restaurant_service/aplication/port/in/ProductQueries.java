package com.example.restaurant_service.aplication.port.in;

import com.example.restaurant_service.aplication.query.ProductWithRestaurantView;
import com.example.restaurant_service.domain.model.product.Product;

import java.util.List;
import java.util.UUID;

public interface ProductQueries {
    Product getProduct(UUID id);
    List<Product> getProductsByRestaurantId(UUID restaurantId );
    List<Product> getAllProducts();
    List<ProductWithRestaurantView> getAllProductsWithRestaurant();
}
