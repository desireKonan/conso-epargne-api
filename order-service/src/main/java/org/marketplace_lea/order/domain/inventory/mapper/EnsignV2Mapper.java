package org.marketplace_lea.order.domain.inventory.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.marketplace_lea.order.common.entities.inventory.EnsignV2Entity;
import org.marketplace_lea.order.domain.inventory.dto.CreateEnsignV2Form;
import org.marketplace_lea.order.domain.inventory.dto.EnsignV2DTO;
import org.marketplace_lea.order.domain.inventory.dto.UpdateEnsignV2Form;

@Mapper(componentModel = "spring")
public interface EnsignV2Mapper {

    @Mapping(target = "imageUrl", expression = "java(entity.getImageUrl())")
    EnsignV2DTO toDTO(EnsignV2Entity entity);

    EnsignV2Entity toEntity(CreateEnsignV2Form form);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void mapToEntity(UpdateEnsignV2Form form, @MappingTarget EnsignV2Entity ensignV2Entity);
}
