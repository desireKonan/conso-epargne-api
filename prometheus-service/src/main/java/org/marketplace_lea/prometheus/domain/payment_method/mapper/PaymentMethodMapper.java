package org.marketplace_lea.prometheus.domain.payment_method.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.marketplace_lea.prometheus.domain.payment_method.dto.PaymentMethodDTO;
import org.marketplace_lea.prometheus.domain.payment_method.entities.PaymentMethodEntity;
import org.marketplace_lea.prometheus.domain.payment_method.form.PaymentMethodCreateForm;
import org.marketplace_lea.prometheus.domain.payment_method.form.PaymentMethodUpdateForm;

@Mapper(componentModel = "spring")
public interface PaymentMethodMapper {
    @Mapping(target = "image", ignore = true) // L'URL est calculée dans le DTO ou séparément
    PaymentMethodEntity toEntity(PaymentMethodCreateForm form);

    @Mapping(target = "image", ignore = true) // L'URL est calculée dans le DTO ou séparément
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget PaymentMethodEntity entity, PaymentMethodUpdateForm form);

    @Mapping(target = "image", ignore = true) // L'URL est calculée dans le DTO ou séparément
    PaymentMethodDTO toDto(PaymentMethodEntity entity);
}