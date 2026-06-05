package org.marketplace_lea.common.repositories;

import org.marketplace_lea.common.entities.LocalityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LocalityJpaRepository extends JpaRepository<LocalityEntity, String>, JpaSpecificationExecutor<LocalityEntity> {
    @Query("SELECT l FROM Locality l WHERE l.deletedAt is not null ORDER BY l.label ASC")
    List<LocalityEntity> getAll(@Param("deleted") boolean deleted);
}