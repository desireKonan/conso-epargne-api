package org.marketplace_lea.common.repositories;

import org.marketplace_lea.common.entities.parameters.ParameterConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ParameterConfigJpaRepository extends JpaRepository<ParameterConfigEntity, String> {
    @Query(value = "SELECT COUNT(config.id) FROM AppConfig config")
    long countParameters();
}
