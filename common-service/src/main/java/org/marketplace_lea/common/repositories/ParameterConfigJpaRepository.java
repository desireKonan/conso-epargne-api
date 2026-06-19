package org.marketplace_lea.common.repositories;

import org.marketplace_lea.common.entities.parameters.ParameterConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ParameterConfigJpaRepository extends JpaRepository<ParameterConfigEntity, Long>, JpaSpecificationExecutor<ParameterConfigEntity> {
    @Query(value = "SELECT COUNT(config.id) FROM AppConfig config")
    long countParameters();


    @Query(value = "SELECT config FROM AppConfig config WHERE config.key =: key")
    Optional<ParameterConfigEntity> getByKey(@Param("key") String key);

    boolean existsByKey(String key);
}
