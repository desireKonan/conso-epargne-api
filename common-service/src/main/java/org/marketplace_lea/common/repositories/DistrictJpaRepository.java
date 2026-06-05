package org.marketplace_lea.common.repositories;

import org.marketplace_lea.common.entities.DistrictEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DistrictJpaRepository extends JpaRepository<DistrictEntity, String>, JpaSpecificationExecutor<DistrictEntity> {
    // Ou avec un nom différent pour garder la même signature
    @Query("SELECT d FROM District d WHERE d.locality.id = :localityId ORDER BY d.label ASC")
    List<DistrictEntity> getAllByLocalityId(@Param("localityId") String localityId);
}