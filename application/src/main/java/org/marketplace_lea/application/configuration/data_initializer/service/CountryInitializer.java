package org.marketplace_lea.application.configuration.data_initializer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.marketplace_lea.application.configuration.data_initializer.dto.CountryConfig;
import org.marketplace_lea.common.entities.CountryV2Entity;
import org.marketplace_lea.common.repositories.CountryJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CountryInitializer {
    private final CountryJpaRepository countryRepository;

    @Transactional
    public void initializeCountries(List<CountryConfig> countryDefs, boolean resetBeforeInit) {
        if (resetBeforeInit) {
            resetCountries();
        }

        if (!hasAnyCountry() && countryDefs != null && !countryDefs.isEmpty()) {
            createCountries(countryDefs);
        } else {
            log.debug("Countries already exist, skipping creation.");
        }
    }

    private void createCountries(List<CountryConfig> defs) {
        List<CountryV2Entity> entities = defs.stream()
                .map(this::toEntity)
                .toList();
        countryRepository.saveAll(entities);
        log.info("Created {} countries.", entities.size());
    }

    private CountryV2Entity toEntity(CountryConfig def) {
        var country = new CountryV2Entity();
        country.setCallingCode(def.callingCode());
        country.setCode(def.code());
        country.setLabel(def.label());
        country.setPhoneNumberLength(def.phoneNumberLength());
        country.setEnabled(def.enabled());
        country.setCreatedAt(LocalDateTime.now());
        return country;
    }

    @Transactional
    public void resetCountries() {
        countryRepository.deleteAllInBatch();
        log.info("All countries have been reset.");
    }

    public boolean hasAnyCountry() {
        return countryRepository.countCountries() > 0;
    }
}