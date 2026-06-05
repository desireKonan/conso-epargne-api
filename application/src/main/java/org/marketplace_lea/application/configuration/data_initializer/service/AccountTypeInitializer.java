package org.marketplace_lea.application.configuration.data_initializer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.marketplace_lea.application.configuration.data_initializer.dto.InitDataConfig;
import org.marketplace_lea.common.entities.account.AccountTypeV2Entity;
import org.marketplace_lea.common.repositories.account.AccountTypeV2JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountTypeInitializer {
    private final AccountTypeV2JpaRepository repository;

    public void initialize(List<InitDataConfig.AccountTypeDef> accountDefs, boolean resetBeforeInit) {
        if (resetBeforeInit && hasData()) {
            resetAll();
        }
        if (!hasData()) {
            createAccountTypes(accountDefs);
        } else {
            log.debug("Account types already present, skipping.");
        }
    }

    private void createAccountTypes(List<InitDataConfig.AccountTypeDef> defs) {
        List<AccountTypeV2Entity> entities = defs.stream()
            .map(this::toEntity)
            .toList();
        repository.saveAll(entities);
        log.info("Created {} account types", entities.size());
    }

    private AccountTypeV2Entity toEntity(InitDataConfig.AccountTypeDef def) {
        var accountType = AccountTypeV2Entity.builder()
            .id(def.getName())
            .label(def.getLabel())
            .withdrawalFees(def.getPercentage())
            .voucherFees(def.getCashback())
            .build();
        accountType.setCreatedAt(LocalDateTime.now());
        return accountType;
    }

    public void resetAll() {
        repository.deleteAllInBatch();
        log.info("Reset all account types");
    }

    public boolean hasData() {
        return repository.countAllTypes() > 0;
    }
}