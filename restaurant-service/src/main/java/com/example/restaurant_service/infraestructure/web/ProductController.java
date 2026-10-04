package com.example.restaurant_service.infraestructure.web;

import com.example.restaurant_service.aplication.port.in.CreateProductCommand;
import com.example.restaurant_service.aplication.port.in.CreateProductUseCase;
import com.example.restaurant_service.aplication.port.in.ProductQueries;
import com.example.restaurant_service.aplication.query.ProductWithRestaurantView;
import com.example.restaurant_service.domain.model.product.Product;
import com.example.restaurant_service.infraestructure.web.dto.CreateProductRequest;
import com.example.restaurant_service.infraestructure.web.dto.ProductResponse;
import com.example.restaurant_service.infraestructure.web.mapper.ProductWebMapper;
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
@RequestMapping("/api/products")
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final ProductQueries productQueries;
    private final ProductWebMapper productWebMapper;

    @PostMapping
    public ResponseEntity<ProductResponse> create(
            @Valid @RequestBody CreateProductRequest request,
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey
    ){
        CreateProductCommand command = productWebMapper.toCommand(request,idempotencyKey);
        Product productCreated = createProductUseCase.createProduct(command);
        ProductResponse productResponse = productWebMapper.toResponse(productCreated);
        return ResponseEntity
                .created(URI.create("/api/orders/" + productResponse.id()))
                .body(productResponse);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProduct() {
        List<Product> products = productQueries.getAllProducts();
        List<ProductResponse> productsResponse = products.stream()
                .map(productWebMapper::toResponse)
                .toList();
        return ResponseEntity.ok(productsResponse);
    }

    @GetMapping("/catalog")
    public ResponseEntity<List<ProductResponse>> getCatalog() {
        List<ProductWithRestaurantView> catalog = productQueries.getAllProductsWithRestaurant();
        List<ProductResponse> productsResponse = catalog.stream()
                .map(productWebMapper::toResponse)
                .toList();
        return ResponseEntity.ok(productsResponse);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable UUID productId) {
        Product product = productQueries.getProduct(productId);
        return ResponseEntity.ok(productWebMapper.toResponse(product));
    }

    @GetMapping("/restaurant/{restaurantIdStr}")
    public ResponseEntity<List<ProductResponse>> getProductByRestaurantId(@PathVariable String restaurantIdStr) {
        UUID restaurantId = UUID.fromString(restaurantIdStr);
        List<Product> products = productQueries.getProductsByRestaurantId(restaurantId);
        List<ProductResponse> productsResponses = products.stream()
                .map(productWebMapper::toResponse)
                .toList();
        return ResponseEntity.ok(productsResponses);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.status(HttpStatus.OK).body("product-controller está funcionando correctamente");
    }
}
