package org.marketplace_lea.order.domain.inventory.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.marketplace_lea.order.common.entities.inventory.ProductV2Entity;
import org.marketplace_lea.order.common.entities.inventory.ShelfV2Entity;
import org.marketplace_lea.order.domain.inventory.dto.CreateProductV2Form;
import org.marketplace_lea.order.domain.inventory.dto.ProductV2DTO;
import org.marketplace_lea.order.domain.inventory.dto.UpdateProductV2Form;

@Mapper(componentModel = "spring")
public interface ProductV2Mapper {

    @Mapping(source = "shelf.id", target = "shelfId")
    @Mapping(source = "shelf", target = "shelfName", qualifiedByName = "mapShelfName")
    @Mapping(source = "shelf.ensign.id", target = "ensignId")
    @Mapping(target = "imageUrl", expression = "java(entity.imageUrl())")
    ProductV2DTO toDTO(ProductV2Entity entity);

    @Mapping(source = "shelfId", target = "shelf.id")
    @Mapping(target = "savingAmount", ignore = true)
    ProductV2Entity toEntity(CreateProductV2Form form);

    @Named("mapShelfName")
    default String mapShelfName(ShelfV2Entity shelf) {
        if (shelf == null) {
            return null;
        }
        return shelf.getLabel();
    }


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void mapToEntity(UpdateProductV2Form form, @MappingTarget ProductV2Entity productV2Entity);
}
