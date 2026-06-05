package org.marketplace_lea.prometheus.domain.template_messages.services;

import org.marketplace_lea.prometheus.domain.template_messages.dto.TransactionNotificationTemplateDto;
import org.marketplace_lea.prometheus.domain.template_messages.form.NotificationTemplateSearchCriteria;
import org.marketplace_lea.prometheus.domain.template_messages.form.TransactionNotificationTemplateForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionNotificationTemplateService {
    Page<TransactionNotificationTemplateDto> search(NotificationTemplateSearchCriteria criteria, Pageable pageable);
    TransactionNotificationTemplateDto findById(Long id);
    TransactionNotificationTemplateDto create(TransactionNotificationTemplateForm form);
    TransactionNotificationTemplateDto update(Long id, TransactionNotificationTemplateForm form);
    void delete(Long id);
}