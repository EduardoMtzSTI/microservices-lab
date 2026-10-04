package com.example.order_service.aplication.query;

import com.example.order_service.aplication.port.out.CustomerSummary;
import com.example.order_service.domain.model.Order;

public record OrderWithCustomerView(Order order, CustomerSummary customer) {}