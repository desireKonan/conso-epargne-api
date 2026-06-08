package org.marketplace_lea.order.domain.order.mapper;

import org.marketplace_lea.common.entities.customer.CustomerV2Entity;
import org.marketplace_lea.order.common.entities.order.OrderV2Entity;
import org.marketplace_lea.order.domain.order.dto.CreateOrderV2Form;
import org.marketplace_lea.order.domain.order.dto.OrderV2DTO;
import org.marketplace_lea.order.domain.order.dto.UpdateOrderV2Form;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface OrderV2Mapper {

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer", target = "customerName", qualifiedByName = "customerName")
    @Mapping(source = "voucher.id", target = "voucherId")
    OrderV2DTO toDTO(OrderV2Entity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "voucher", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "validatedAt", ignore = true)
    @Mapping(target = "canceledAt", ignore = true)
    @Mapping(target = "paidAt", ignore = true)
    @Mapping(target = "deliveredAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "delivered", ignore = true)
    OrderV2Entity toEntity(CreateOrderV2Form form);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "voucher", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "validatedAt", ignore = true)
    @Mapping(target = "canceledAt", ignore = true)
    @Mapping(target = "paidAt", ignore = true)
    @Mapping(target = "deliveredAt", ignore = true)
    @Mapping(target = "delivered", ignore = true)
    @Mapping(target = "status", ignore = true)
    void mapToEntity(UpdateOrderV2Form form, @MappingTarget OrderV2Entity entity);

    @Named("customerName")
    default String customerName(CustomerV2Entity customer) {
        if (customer == null || customer.getAccount() == null) {
            return null;
        }
        return customer.getAccount().getLogin();
    }
}
