package org.marketplace_lea.common.repositories.inventory;

import org.marketplace_lea.common.entities.inventory.EnsignV2Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EnsignV2JpaRepository extends JpaRepository<EnsignV2Entity, String>, JpaSpecificationExecutor<EnsignV2Entity> {
    Optional<EnsignV2Entity> findByRank(int rank);

    @Modifying
    @Query("UPDATE EnsignV2 e set e.deletedAt = CURRENT_TIMESTAMP where e.id = :id")
    void softDeleteById(@Param("id") String id);
}