package com.example.order_service.aplication.port.out;

import java.util.function.Supplier;

public interface UnitOfWork {
    <T> T execute(Supplier<T> action);
}
