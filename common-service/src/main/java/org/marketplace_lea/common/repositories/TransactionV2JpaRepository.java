package org.marketplace_lea.common.repositories;

import org.marketplace_lea.common.entities.transaction.TransactionV2Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TransactionV2JpaRepository extends JpaRepository<TransactionV2Entity, String>, JpaSpecificationExecutor<TransactionV2Entity> {
}
