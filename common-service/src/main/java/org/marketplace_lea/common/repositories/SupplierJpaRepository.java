package org.marketplace_lea.common.repositories;

import org.marketplace_lea.common.entities.SupplierV2Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface SupplierJpaRepository extends JpaRepository<SupplierV2Entity, Long>, JpaSpecificationExecutor<SupplierV2Entity> {
    @Query(value = "SELECT COUNT(supplier.id) FROM SupplierV2 supplier")
    long countSuppliers();
}
