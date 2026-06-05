package org.marketplace_lea.application.configuration.data_initializer.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.marketplace_lea.application.configuration.data_initializer.dto.ParameterConfigs;
import org.marketplace_lea.common.entities.parameters.ParameterConfigEntity;
import org.marketplace_lea.common.repositories.ParameterConfigJpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParameterInitializer {
    private final ParameterConfigJpaRepository parameterRepository;

    @Transactional
    public void initializeParameters(List<ParameterConfigs> parameterDefs, boolean resetBeforeInit) {
        if (resetBeforeInit) {
            resetParameters();
        }

        if (!hasAnyParameter() && parameterDefs != null && !parameterDefs.isEmpty()) {
            createParameters(parameterDefs);
        } else {
            log.debug("Parameters already exist, skipping creation.");
        }
    }

    private void createParameters(List<ParameterConfigs> defs) {
        List<ParameterConfigEntity> entities = defs.stream()
                .map(this::toEntity)
                .toList();
        parameterRepository.saveAll(entities);
        log.info("Created {} parameters.", entities.size());
    }

    private ParameterConfigEntity toEntity(ParameterConfigs def) {
        ParameterConfigEntity entity = new ParameterConfigEntity();
        entity.setKey(def.getKey());
        entity.setValue(def.getValue());
        entity.setDescription(def.getDescription());
        entity.setDataType(def.getDataType().name());
        entity.setCreatedAt(LocalDateTime.now());
        return entity;
    }

    public void resetParameters() {
        parameterRepository.deleteAllInBatch();
        log.info("All parameters have been reset.");
    }

    public boolean hasAnyParameter() {
        return parameterRepository.countParameters() > 0;
    }
}