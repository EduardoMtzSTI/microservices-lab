package com.example.order_service.aplication.port.out;

import java.util.UUID;

public interface ProductCatalogPort {
    ProductPrice getCurrentPrice(UUID productId);
}
