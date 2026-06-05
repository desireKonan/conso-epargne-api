package org.marketplace_lea.prometheus.domain.localization.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.marketplace_lea.common.entities.DistrictEntity;
import org.marketplace_lea.prometheus.domain.localization.dto.DistrictDto;
import org.marketplace_lea.prometheus.domain.localization.form.DistrictCreateForm;
import org.marketplace_lea.prometheus.domain.localization.form.DistrictUpdateForm;

@Mapper(componentModel = "spring")
public interface DistrictMapper {

    @Mapping(source = "localityId", target = "locality.id")
    DistrictEntity toEntity(DistrictCreateForm form);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "localityId", target = "locality.id")
    void updateEntity(@MappingTarget DistrictEntity entity, DistrictUpdateForm form);

    @Mapping(source = "locality.id", target = "localityId")
    @Mapping(source = "locality.label", target = "localityLabel")
    @Mapping(source = "fees", target = "fees")
    DistrictDto toDto(DistrictEntity entity);
}