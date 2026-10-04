package com.example.payment_service.infrastructure.persistence.mapper;

import com.example.payment_service.domain.model.Money;
import com.example.payment_service.domain.model.Payment;
import com.example.payment_service.domain.model.PaymentSnapshot;
import com.example.payment_service.infrastructure.persistence.entity.PaymentJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Currency;

@Mapper(componentModel = "spring")
public interface PaymentPersistenceMapper {

    // toSnapShot
    default Money toMoney(Long amount, String currencyCode) {
        return new Money(amount, Currency.getInstance(currencyCode));
    }

    @Mapping(target = "amount", expression = "java(toMoney(entity.getAmount(), entity.getCurrency()))")
    PaymentSnapshot toSnapShot(PaymentJpaEntity entity);

    // toDomain
    default Payment toDomain(PaymentJpaEntity entity){
        return Payment.reconstitute(toSnapShot(entity));
    }

    // toJpaEntity

    @Named("moneyToAmount")
    default Long moneyToLong(Money money) {
        return money.amount();
    }

    @Named("moneyToCurrency")
    default String moneyToCurrency(Money money) {
        return money.currency().getCurrencyCode();
    }

    @Mapping(target = "amount", source = "amount", qualifiedByName = "moneyToAmount")
    @Mapping(target = "currency", source = "amount", qualifiedByName = "moneyToCurrency")
    PaymentJpaEntity toJpaEntity(Payment payment);

}
