package org.marketplace_lea.common.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.marketplace_lea.common.dtos.wallets.WalletDTO;
import org.marketplace_lea.common.entities.wallet.WalletV2Entity;

import java.time.LocalDateTime;
import java.util.UUID;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        imports = {LocalDateTime.class, UUID.class})
public interface WalletMapper {
    @Mapping(source = "account.id", target = "accountId")
    @Mapping(source = "account.login", target = "login")
    WalletDTO toDto(WalletV2Entity walletV2Entity);

    @Mapping(source = "accountId", target = "account.id")
    @Mapping(source = "login", target = "account.login")
    WalletV2Entity toEntity(WalletDTO dto);
}
