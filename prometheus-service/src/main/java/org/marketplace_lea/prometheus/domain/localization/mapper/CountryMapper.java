package org.marketplace_lea.prometheus.domain.localization.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.marketplace_lea.common.entities.CountryV2Entity;
import org.marketplace_lea.prometheus.domain.localization.dto.CountryDto;
import org.marketplace_lea.prometheus.domain.localization.form.CountryCreateForm;
import org.marketplace_lea.prometheus.domain.localization.form.CountryUpdateForm;

@Mapper(componentModel = "spring")
public interface CountryMapper {

    CountryV2Entity toEntity(CountryCreateForm form);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget CountryV2Entity entity, CountryUpdateForm form);

    CountryDto toDto(CountryV2Entity entity);
}