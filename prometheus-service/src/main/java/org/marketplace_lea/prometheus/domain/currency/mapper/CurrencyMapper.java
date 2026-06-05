package org.marketplace_lea.prometheus.domain.currency.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.marketplace_lea.common.entities.CurrencyV2Entity;
import org.marketplace_lea.prometheus.domain.currency.dto.CurrencyDto;
import org.marketplace_lea.prometheus.domain.currency.form.CurrencyCreateForm;
import org.marketplace_lea.prometheus.domain.currency.form.CurrencyUpdateForm;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {
    CurrencyV2Entity toEntity(CurrencyCreateForm form);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget CurrencyV2Entity entity, CurrencyUpdateForm form);

    CurrencyDto toDto(CurrencyV2Entity entity);
}