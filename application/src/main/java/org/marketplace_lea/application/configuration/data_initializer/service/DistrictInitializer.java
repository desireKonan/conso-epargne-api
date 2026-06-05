package org.marketplace_lea.application.configuration.data_initializer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.marketplace_lea.application.configuration.data_initializer.dto.DistrictConfig;
import org.marketplace_lea.common.entities.DistrictEntity;
import org.marketplace_lea.common.entities.LocalityEntity;
import org.marketplace_lea.common.repositories.DistrictJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DistrictInitializer {
    private final DistrictJpaRepository districtRepository;

    @Transactional
    public void initializeDistricts(List<DistrictConfig> districtDefs, boolean resetBeforeInit) {
        if (resetBeforeInit) {
            resetDistricts();
        }

        if (!hasAnyDistrict() && districtDefs != null && !districtDefs.isEmpty()) {
            createDistricts(districtDefs);
        } else {
            log.debug("Districts already exist, skipping creation.");
        }
    }

    private void createDistricts(List<DistrictConfig> defs) {
        List<DistrictEntity> entities = defs.stream()
                .map(this::toEntity)
                .toList();
        districtRepository.saveAll(entities);
        log.info("Created {} districts.", entities.size());
    }

    private DistrictEntity toEntity(DistrictConfig def) {
        var district = new DistrictEntity();
        district.setLabel(def.label());

        var locality = new LocalityEntity();
        locality.setId(def.localityId());
        district.setLocality(locality);

        district.setCreatedAt(LocalDateTime.now());
        // createdAt/updatedAt auto-gérés
        return district;
    }

    @Transactional
    public void resetDistricts() {
        districtRepository.deleteAllInBatch();
        log.info("All districts have been reset.");
    }

    public boolean hasAnyDistrict() {
        return districtRepository.count() > 0;
    }
}