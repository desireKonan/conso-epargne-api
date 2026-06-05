package org.marketplace_lea.prometheus.domain.localization.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.marketplace_lea.common.entities.LocalityEntity;
import org.marketplace_lea.prometheus.domain.localization.dto.LocalityDto;
import org.marketplace_lea.prometheus.domain.localization.form.LocalityCreateForm;
import org.marketplace_lea.prometheus.domain.localization.form.LocalityUpdateForm;

@Mapper(componentModel = "spring")
public interface LocalityMapper {

    LocalityEntity toEntity(LocalityCreateForm form);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget LocalityEntity entity, LocalityUpdateForm form);

    LocalityDto toDto(LocalityEntity entity);
}