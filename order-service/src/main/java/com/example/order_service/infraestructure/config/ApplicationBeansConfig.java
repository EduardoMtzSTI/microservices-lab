package com.example.order_service.infraestructure.config;

import com.example.order_service.aplication.port.in.AcceptOrderUseCase;
import com.example.order_service.aplication.port.in.ConfirmOrderPaymentUseCase;
import com.example.order_service.aplication.port.in.FailOrderPaymentUseCase;
import com.example.order_service.aplication.port.in.MarkOrderReadyForPickupUseCase;
import com.example.order_service.aplication.port.out.*;
import com.example.order_service.aplication.service.OrderApplicationService;
import com.example.order_service.infraestructure.catalog.RestaurantServiceProductCatalogAdapter;
import com.example.order_service.infraestructure.customer.CustomerServiceLookupAdapter;
import com.example.order_service.infraestructure.messaging.consumer.OrderReadyEventListener;
import com.example.order_service.infraestructure.messaging.consumer.PaymentConfirmedEventListener;
import com.example.order_service.infraestructure.messaging.consumer.PaymentFailedEventListener;
import com.example.order_service.infraestructure.messaging.consumer.OrderAcceptedEventListener;
import com.example.order_service.infraestructure.persistence.SpringUnitOfWork;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class ApplicationBeansConfig {

    @Bean
    public UnitOfWork unitOfWork(PlatformTransactionManager transactionManager){
        return new SpringUnitOfWork(transactionManager);
    }

    @Bean
    public ProductCatalogPort productCatalogPort(
            @Value("${services.restaurant.base-url}") String baseUrl
    ) {
        RestClient restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
        return new RestaurantServiceProductCatalogAdapter(restClient);
    }

    @Bean CustomerLookupPort customerLookupPort(
            @Value("${services.customer.base-url}") String baseUrl
    ){
        RestClient restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
        return new CustomerServiceLookupAdapter(restClient);
    }

    @Bean
    public OrderApplicationService orderApplicationService(
            OrderRepositoryPort orderRepository,
            ProductCatalogPort productCatalog,
            OrderEventPublisherPort orderEventPublisher,
            CustomerLookupPort customerLookupPort,
            UnitOfWork unitOfWork
    ) {
        return new OrderApplicationService(orderRepository,productCatalog,orderEventPublisher,customerLookupPort,unitOfWork);
    }

    @Bean
    public PaymentConfirmedEventListener paymentConfirmedEventListener(
            ConfirmOrderPaymentUseCase confirmOrderPaymentUseCase,
            ObjectMapper objectMapper)
    {
        return new PaymentConfirmedEventListener(confirmOrderPaymentUseCase, objectMapper);
    }

    @Bean
    public PaymentFailedEventListener paymentFailedEventListener(
            FailOrderPaymentUseCase failOrderPaymentUseCase,
            ObjectMapper objectMapper
    ) {
        return new PaymentFailedEventListener(failOrderPaymentUseCase, objectMapper);
    }

    @Bean public OrderAcceptedEventListener restaurantAcceptedEventListener(
            AcceptOrderUseCase acceptOrderUseCase,
            ObjectMapper objectMapper
    ){
        return new OrderAcceptedEventListener(acceptOrderUseCase,objectMapper);
    }

    @Bean public OrderReadyEventListener orderReadyEventListener(
            MarkOrderReadyForPickupUseCase markOrderReadyForPickupUseCase,
            ObjectMapper objectMapper
    ){
        return new OrderReadyEventListener(markOrderReadyForPickupUseCase,objectMapper);
    }

}
