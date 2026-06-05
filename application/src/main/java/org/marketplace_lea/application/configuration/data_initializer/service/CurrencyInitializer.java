package org.marketplace_lea.application.configuration.data_initializer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.marketplace_lea.application.configuration.data_initializer.dto.InitDataConfig;
import org.marketplace_lea.common.entities.CurrencyV2Entity;
import org.marketplace_lea.common.repositories.CurrencyJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyInitializer {
    private final CurrencyJpaRepository currencyRepository;

    @Transactional
    public void initializeCurrencies(List<InitDataConfig.CurrencyDef> currencyDefs, boolean resetBeforeInit) {
        if (resetBeforeInit) {
            resetCurrencies();
        }

        if (!hasAnyCurrency() && !currencyDefs.isEmpty()) {
            createCurrencies(currencyDefs);
        } else {
            log.debug("Currencies already exist, skipping creation.");
        }
    }

    private void createCurrencies(List<InitDataConfig.CurrencyDef> defs) {
        List<CurrencyV2Entity> entities = defs.stream()
                .map(this::toEntity)
                .toList();
        currencyRepository.saveAll(entities);
        log.info("Created {} currencies.", entities.size());
    }

    private CurrencyV2Entity toEntity(InitDataConfig.CurrencyDef def) {
        CurrencyV2Entity currency = new CurrencyV2Entity();
        currency.setCode(def.getCode());
        currency.setLabel(def.getName());
        currency.setDescription(def.getDescription());
        currency.setCreatedAt(LocalDateTime.now());
        return currency;
    }

    @Transactional
    public void resetCurrencies() {
        currencyRepository.deleteAllInBatch();
        log.info("All currencies have been reset.");
    }

    public boolean hasAnyCurrency() {
        return currencyRepository.countCurrencies() > 0;
    }
}