package org.marketplace_lea.prometheus.domain.template_messages.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.marketplace_lea.common.entities.transaction.TransactionNotificationTemplateEntity;
import org.marketplace_lea.prometheus.domain.template_messages.dto.TransactionNotificationTemplateDto;
import org.marketplace_lea.prometheus.domain.template_messages.form.TransactionNotificationTemplateForm;

@Mapper(componentModel = "spring")
public interface TransactionNotificationTemplateMapper {
    TransactionNotificationTemplateEntity toEntity(TransactionNotificationTemplateForm form);

    TransactionNotificationTemplateDto toDto(TransactionNotificationTemplateEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget TransactionNotificationTemplateEntity entity, TransactionNotificationTemplateForm form);
}
