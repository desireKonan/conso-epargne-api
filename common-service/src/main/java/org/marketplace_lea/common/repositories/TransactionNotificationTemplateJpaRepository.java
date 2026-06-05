package org.marketplace_lea.common.repositories;

import org.marketplace_lea.common.entities.transaction.TransactionNotificationTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionNotificationTemplateJpaRepository extends JpaRepository<TransactionNotificationTemplateEntity, String> {
}
