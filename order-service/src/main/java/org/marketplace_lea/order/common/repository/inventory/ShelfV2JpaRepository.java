package org.marketplace_lea.order.common.repository.inventory;

import org.marketplace_lea.order.common.entities.inventory.ShelfV2Entity;import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ShelfV2JpaRepository extends JpaRepository<ShelfV2Entity, String>, JpaSpecificationExecutor<ShelfV2Entity> {
    Optional<ShelfV2Entity> findByRank(int rank);

    @Query("SELECT s FROM ShelfV2 s WHERE s.parent IS NULL AND s.deletedAt IS NULL AND s.ensign.id = :ensId ORDER BY s.rank ASC")
    List<ShelfV2Entity> findRootShelvesByEnsign(@Param("ensId") String ensId);

    @Query("SELECT s FROM ShelfV2 s WHERE s.ensign.id = :ensId AND s.deletedAt IS NULL ORDER BY s.rank ASC")
    List<ShelfV2Entity> findByEnsignId(@Param("ensId") String ensId);

    @Modifying
    @Query("UPDATE ShelfV2 e set e.deletedAt = CURRENT_TIMESTAMP where e.id = :id")
    void softDeleteById(@Param("id") String id);
}