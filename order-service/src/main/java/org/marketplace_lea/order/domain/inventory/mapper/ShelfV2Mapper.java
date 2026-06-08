package org.marketplace_lea.order.domain.inventory.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.marketplace_lea.order.common.entities.inventory.EnsignV2Entity;
import org.marketplace_lea.order.common.entities.inventory.ShelfV2Entity;
import org.marketplace_lea.order.domain.inventory.dto.CreateShelfV2Form;
import org.marketplace_lea.order.domain.inventory.dto.ShelfV2DTO;
import org.marketplace_lea.order.domain.inventory.dto.UpdateShelfV2Form;

@Mapper(componentModel = "spring")
public interface ShelfV2Mapper {

    @Mapping(source = "parent.id", target = "parentId")
    @Mapping(source = "parent", target = "parentLabel", qualifiedByName = "mapParentLabel")
    @Mapping(source = "ensign.id", target = "ensignId")
    @Mapping(source = "ensign", target = "ensignLabel", qualifiedByName = "mapEnsignLabel")
    @Mapping(target = "imageUrl", expression = "java(entity.getImageUrl())")
    ShelfV2DTO toDTO(ShelfV2Entity entity);

    @Mapping(source = "parentId", target = "parent.id")
    @Mapping(source = "ensignId", target = "ensign.id")
    ShelfV2Entity toEntity(CreateShelfV2Form form);

    @Named("mapParentLabel")
    default String mapParentLabel(ShelfV2Entity parent) {
        if (parent == null) {
            return null;
        }
        return parent.getLabel();
    }

    @Named("mapEnsignLabel")
    default String mapEnsignLabel(EnsignV2Entity ensign) {
        if (ensign == null) {
            return null;
        }
        return ensign.getLabel();
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void mapToEntity(UpdateShelfV2Form form, @MappingTarget ShelfV2Entity shelfV2Entity);
}
