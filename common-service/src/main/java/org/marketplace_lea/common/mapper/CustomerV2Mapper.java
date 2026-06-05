package org.marketplace_lea.common.mapper;

import org.marketplace_lea.common.dtos.CustomerTokenInfo;
import org.marketplace_lea.common.dtos.CustomerV2DTO;
import org.marketplace_lea.common.entities.customer.CustomerV2Entity;
import org.marketplace_lea.common.forms.CreateCustomerForm;
import org.marketplace_lea.common.forms.RegistrationV2Form;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerV2Mapper {
    @Mapping(source = "form.firstname", target = "firstName")
    @Mapping(source = "form.lastname", target = "lastName")
    @Mapping(source = "form.email", target = "email")
    @Mapping(source = "form.accountId", target = "account.id")
    CustomerV2Entity toEntity(CreateCustomerForm form);

    @Mapping(target = "address", ignore = true)
    @Mapping(source = "parentCode", target = "account.affiliationCode")
    @Mapping(source = "login", target = "account.login")
    @Mapping(source = "countryCode", target = "account.countryCode")
    @Mapping(source = "accountTypeId", target = "account.accountType.id")
    CustomerV2Entity toEntity(RegistrationV2Form form);

    @Mapping(source = "account.accountType.id", target = "account.accountTypeId")
    CustomerV2DTO toDTO(CustomerV2Entity entity);

    @Mapping(source = "account.accountType.id", target = "account.accountTypeId")
    CustomerTokenInfo toInfo(CustomerV2Entity entity);

    @Mapping(source = "account.accountTypeId", target = "account.accountType.id")
    CustomerV2Entity toEntity(CustomerV2DTO dto);
}