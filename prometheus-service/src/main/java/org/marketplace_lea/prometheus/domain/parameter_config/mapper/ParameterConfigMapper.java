package org.marketplace_lea.prometheus.domain.parameter_config.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.marketplace_lea.common.entities.parameters.ParameterConfigEntity;
import org.marketplace_lea.prometheus.domain.parameter_config.dto.ParameterConfigDto;
import org.marketplace_lea.prometheus.domain.parameter_config.form.ParameterConfigCreateForm;
import org.marketplace_lea.prometheus.domain.parameter_config.form.ParameterConfigUpdateForm;

@Mapper(componentModel = "spring")
public interface ParameterConfigMapper {
    // CreateForm → Entity
    ParameterConfigEntity toEntity(ParameterConfigCreateForm form);

    // Entity → DTO
    ParameterConfigDto toDto(ParameterConfigEntity entity);

    // UpdateForm → Entity (pour mise à jour partielle)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget ParameterConfigEntity entity, ParameterConfigUpdateForm form);
}