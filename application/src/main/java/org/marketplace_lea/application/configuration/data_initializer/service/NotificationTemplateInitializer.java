package org.marketplace_lea.application.configuration.data_initializer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.marketplace_lea.application.configuration.data_initializer.dto.TransactionTemplateMessageConfig;
import org.marketplace_lea.common.entities.transaction.TransactionNotificationTemplateEntity;
import org.marketplace_lea.common.repositories.TransactionNotificationTemplateJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationTemplateInitializer {
    private final TransactionNotificationTemplateJpaRepository templateRepository;

    @Transactional
    public void initializeTemplates(List<TransactionTemplateMessageConfig> templateDefs, boolean resetBeforeInit) {
        if (resetBeforeInit) {
            resetTemplates();
        }

        if (!hasAnyTemplate() && templateDefs != null && !templateDefs.isEmpty()) {
            createTemplates(templateDefs);
        } else {
            log.debug("Notification templates already exist, skipping creation.");
        }
    }

    private void createTemplates(List<TransactionTemplateMessageConfig> defs) {
        List<TransactionNotificationTemplateEntity> entities = defs.stream()
                .map(this::toEntity)
                .toList();
        templateRepository.saveAll(entities);
        log.info("Created {} notification templates.", entities.size());
    }

    private TransactionNotificationTemplateEntity toEntity(TransactionTemplateMessageConfig def) {
        var transactionTemplateNotification = new TransactionNotificationTemplateEntity();
        transactionTemplateNotification.setKey(def.id());
        transactionTemplateNotification.setTitles(def.titles());
        transactionTemplateNotification.setMessages(def.messages());
        transactionTemplateNotification.setTransactionType(def.transactionType());
        transactionTemplateNotification.setActive(true);
        transactionTemplateNotification.setCreatedAt(LocalDateTime.now());
        return transactionTemplateNotification;
    }

    @Transactional
    public void resetTemplates() {
        templateRepository.deleteAllInBatch();
        log.info("All notification templates have been reset.");
    }

    public boolean hasAnyTemplate() {
        return templateRepository.count() > 0;
    }
}