package org.marketplace_lea.common.mapper;

import org.marketplace_lea.common.dtos.ConsoSubscriptionDTO;
import org.marketplace_lea.common.entities.customer.CustomerV2Entity;
import org.marketplace_lea.common.entities.subscription.ConsoSubscriptionV2Entity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ConsoSubscriptionMapper {
    
    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer", target = "customerName", qualifiedByName = "mapCustomerName")
    ConsoSubscriptionDTO toDTO(ConsoSubscriptionV2Entity subscription);
    
    @Mapping(target = "customer", ignore = true)
    ConsoSubscriptionV2Entity toEntity(ConsoSubscriptionDTO dto);
    
    @Named("mapCustomerName")
    default String mapCustomerName(CustomerV2Entity customer) {
        if (customer == null) {
            return null;
        }
        // Adaptez cette logique selon les champs disponibles dans Customer
        return customer._fullname(); // ou customer.getFirstName() + " " + customer.getLastName()
    }
}