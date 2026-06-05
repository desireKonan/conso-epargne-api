package org.marketplace_lea.common.repositories;

import org.marketplace_lea.common.entities.transaction.TransactionNotificationTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TransactionNotificationTemplateJpaRepository extends JpaRepository<TransactionNotificationTemplateEntity, Long>, JpaSpecificationExecutor<TransactionNotificationTemplateEntity> {
}
