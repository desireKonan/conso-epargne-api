package org.marketplace_lea.order.common.repository.inventory;

import org.marketplace_lea.order.common.entities.inventory.ProductV2Entity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductV2JpaRepository extends JpaRepository<ProductV2Entity, String>, JpaSpecificationExecutor<ProductV2Entity> {
    @Query("SELECT p FROM ProductV2 p WHERE p.shelf.ensign.id = :ensignId AND p.deletedAt IS NOT NULL ORDER BY :#{#sort}")
    List<ProductV2Entity> getByEnsignAndNotDeleted(@Param("ensignId") String ensId, @Param("sort") Sort sort);

    @Query("SELECT p FROM ProductV2 p WHERE p.deletedAt IS NOT NULL ORDER BY :#{#sort}")
    List<ProductV2Entity> getBySort(@Param("sort") Sort sort);

    @Query("SELECT p FROM ProductV2 p WHERE p.rank = :rank AND p.deletedAt IS NOT NULL")
    Optional<ProductV2Entity> findByRank(@Param("rank") int rank);

    @Query("SELECT p FROM ProductV2 p WHERE p.soldOut = :soldOut AND p.deletedAt IS NOT NULL")
    List<ProductV2Entity> getBySoldOut(@Param("soldOut") boolean soldOut);

    @Modifying
    @Query("UPDATE ProductV2 SET rank = rank + 1 WHERE rank BETWEEN ?1 AND ?2")
    void incrementRankBetween(Integer startRank, Integer endRank);

    @Modifying
    @Query("UPDATE ProductV2 SET rank = rank - 1 WHERE rank BETWEEN ?1 AND ?2")
    void decrementRankBetween(Integer startRank, Integer endRank);
}