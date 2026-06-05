package org.marketplace_lea.common.repositories;

import org.marketplace_lea.common.entities.CurrencyV2Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface CurrencyJpaRepository extends JpaRepository<CurrencyV2Entity, String>, JpaSpecificationExecutor<CurrencyV2Entity> {
    @Query(value = "SELECT COUNT(currency.id) FROM Currency currency")
    long countCurrencies();
}