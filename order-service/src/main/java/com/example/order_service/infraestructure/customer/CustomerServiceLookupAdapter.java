package com.example.order_service.infraestructure.customer;

import com.example.order_service.aplication.port.out.CustomerLookupPort;
import com.example.order_service.aplication.port.out.CustomerSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.UUID;

@RequiredArgsConstructor
public class CustomerServiceLookupAdapter implements CustomerLookupPort {

    private final RestClient restClient;

    @Override
    public CustomerSummary getCustomerSummary(UUID customerId) {
        try {

            CustomerApiResponse response = restClient.get()
                    .uri("/api/customers/{id}", customerId)
                    .retrieve()
                    .body(CustomerApiResponse.class);

            assert response != null;

            return CustomerSummary.builder()
                    .id(response.id())
                    .fullName(response.fullName())
                    .phone(response.number())
                    .address(response.address()).build();

        } catch (HttpClientErrorException.NotFound e) {
            return CustomerSummary.builder().build();
        } catch (RestClientException e) {
            throw new IllegalStateException("No se pudo consultar customer-service", e);
        }
    }

}
