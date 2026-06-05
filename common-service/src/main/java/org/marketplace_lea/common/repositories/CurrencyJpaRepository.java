package org.marketplace_lea.common.repositories;

import org.marketplace_lea.common.entities.CurrencyV2Entity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyJpaRepository extends JpaRepository<CurrencyV2Entity, String> {
}