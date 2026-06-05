package org.marketplace_lea.application.configuration.data_initializer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.marketplace_lea.application.configuration.data_initializer.dto.LocalityConfig;
import org.marketplace_lea.common.entities.LocalityEntity;
import org.marketplace_lea.common.repositories.LocalityJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocalityInitializer {
    private final LocalityJpaRepository localityRepository;

    @Transactional
    public void initializeLocalities(List<LocalityConfig> localityDefs, boolean resetBeforeInit) {
        if (resetBeforeInit) {
            resetLocalities();
        }

        if (!hasAnyLocality() && localityDefs != null && !localityDefs.isEmpty()) {
            createLocalities(localityDefs);
        } else {
            log.debug("Localities already exist, skipping creation.");
        }
    }

    private void createLocalities(List<LocalityConfig> defs) {
        List<LocalityEntity> entities = defs.stream()
                .map(this::toEntity)
                .toList();
        localityRepository.saveAll(entities);
        log.info("Created {} localities.", entities.size());
    }

    private LocalityEntity toEntity(LocalityConfig def) {
        var locality = new LocalityEntity();
        locality.setId(def.id());
        locality.setFees(def.fees());
        locality.setLabel(def.label());
        locality.setCreatedAt(LocalDateTime.now());
        return locality;
    }

    @Transactional
    public void resetLocalities() {
        localityRepository.deleteAllInBatch();
        log.info("All localities have been reset.");
    }

    public boolean hasAnyLocality() {
        return localityRepository.count() > 0;
    }
}