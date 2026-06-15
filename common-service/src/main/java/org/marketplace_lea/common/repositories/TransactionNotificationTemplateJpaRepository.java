package org.marketplace_lea.common.repositories;

import org.marketplace_lea.common.entities.transaction.TransactionNotificationTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TransactionNotificationTemplateJpaRepository extends JpaRepository<TransactionNotificationTemplateEntity, Long>, JpaSpecificationExecutor<TransactionNotificationTemplateEntity> {
    @Query(value = "SELECT template FROM TransactionNotificationTemplate template WHERE template.key = :key")
    Optional<TransactionNotificationTemplateEntity> getByKey(@Param("key") String key);
}
