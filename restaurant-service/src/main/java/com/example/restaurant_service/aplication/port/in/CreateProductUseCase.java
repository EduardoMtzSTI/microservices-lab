package com.example.restaurant_service.aplication.port.in;

import com.example.restaurant_service.domain.model.product.Product;

public interface CreateProductUseCase {
    Product createProduct(CreateProductCommand command);
}
