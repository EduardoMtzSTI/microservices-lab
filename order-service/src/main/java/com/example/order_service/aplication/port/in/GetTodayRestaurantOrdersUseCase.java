package com.example.order_service.aplication.port.in;

import com.example.order_service.aplication.query.OrderWithCustomerView;

import java.util.List;
import java.util.UUID;

public interface GetTodayRestaurantOrdersUseCase {
    List<OrderWithCustomerView> getTodayRestaurantOrders(UUID restaurantId);
}
