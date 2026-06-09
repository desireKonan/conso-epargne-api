package org.marketplace_lea.application.configuration.data_initializer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.marketplace_lea.application.configuration.data_initializer.dto.InitDataConfig;
import org.marketplace_lea.prometheus.domain.payment_method.entities.PaymentMethodEntity;
import org.marketplace_lea.prometheus.domain.payment_method.repository.PaymentMethodJpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentMethodInitializer {
    private final PaymentMethodJpaRepository repository;

    public void initialize(List<InitDataConfig.PaymentMethodDef> paymentMethodDefs, boolean resetBeforeInit) {
        if (resetBeforeInit && hasData()) {
            resetAll();
        }
        if (!hasData()) {
            createPaymentMethods(paymentMethodDefs);
        } else {
            log.debug("Method Payments already present, skipping.");
        }
    }

    private void createPaymentMethods(List<InitDataConfig.PaymentMethodDef> defs) {
        List<PaymentMethodEntity> entities = defs.stream()
                .map(this::toEntity)
                .toList();
        repository.saveAll(entities);
        log.info("Created {} payment methods", entities.size());
    }

    private PaymentMethodEntity toEntity(InitDataConfig.PaymentMethodDef def) {
        var paymentMethod = PaymentMethodEntity.builder()
                .label(def.getName())
                .available(def.isActive())
                .online(def.isOnline())
                .provider(def.getProvider())
                .build();
        paymentMethod.setCreatedAt(LocalDateTime.now());
        return paymentMethod;
    }

    public void resetAll() {
        repository.deleteAllInBatch();
        log.info("Reset all payment methods");
    }

    public boolean hasData() {
        return repository.count() > 0;
    }
}
