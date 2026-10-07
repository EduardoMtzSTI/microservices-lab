package com.example.restaurant_service.infrastructure.persistence;

import com.example.restaurant_service.aplication.port.out.UnitOfWork;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.function.Supplier;

public class SpringUnitOfWork implements UnitOfWork {

    private final TransactionTemplate transactionTemplate;

    public SpringUnitOfWork(PlatformTransactionManager transactionManager) {
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    @Override
    public <T> T execute(Supplier<T> action) {
        return transactionTemplate.execute(status->action.get());
    }

}
