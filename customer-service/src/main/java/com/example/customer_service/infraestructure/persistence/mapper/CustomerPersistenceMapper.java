package com.example.customer_service.infraestructure.persistence.mapper;

import com.example.customer_service.domain.model.Customer;
import com.example.customer_service.domain.model.CustomerSnapshot;
import com.example.customer_service.infraestructure.persistence.entity.CustomerJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerPersistenceMapper{

    CustomerJpaEntity toJpaEntity(Customer customer);

    CustomerSnapshot toSnapshot(CustomerJpaEntity entity);

    default Customer toDomain(CustomerJpaEntity entity) {
        return Customer.reconstitute(toSnapshot(entity));
    }

}
