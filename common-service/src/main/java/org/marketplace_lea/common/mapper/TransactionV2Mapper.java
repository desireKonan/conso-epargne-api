package org.marketplace_lea.common.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.marketplace_lea.common.dtos.transactions.TransactionDTO;
import org.marketplace_lea.common.entities.transaction.TransactionStatus;
import org.marketplace_lea.common.entities.transaction.TransactionType;
import org.marketplace_lea.common.entities.transaction.TransactionV2Entity;
import org.marketplace_lea.common.entities.wallet.WalletV2Entity;
import org.marketplace_lea.common.forms.transactions.TransactionV2CreateForm;

import java.time.LocalDateTime;
import java.util.UUID;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        imports = {LocalDateTime.class, UUID.class})
public interface TransactionV2Mapper {
    // ============================================================
    // ENTITY TO DTO
    // ============================================================
    @Mapping(target = "transactionStatus", ignore = true)
    @Mapping(target = "transactionType", ignore = true)
    TransactionDTO toDto(TransactionV2Entity entity);

    @Mapping(source = "sourceWalletId", target = "source.id")
    @Mapping(source = "destinationWalletId", target = "destination.id")
    @Mapping(target = "transactionStatus", ignore = true)
    @Mapping(target = "transactionType", ignore = true)
    TransactionV2Entity toDto(TransactionV2CreateForm form);


    // ============================================================
    // DTO TO ENTITY
    // ============================================================
    
    TransactionV2Entity toEntity(TransactionDTO dto);

    // ============================================================
    // UPDATE METHODS (FOR PATCH/PUT)
    // ============================================================
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(TransactionDTO dto, @MappingTarget TransactionV2Entity entity);

    // ============================================================
    // CUSTOM MAPPING METHODS
    // ============================================================
    
    @Named("mapToWallet")
    static WalletV2Entity mapToWallet(String walletId) {
        if (walletId == null || walletId.isEmpty()) {
            return null;
        }
        WalletV2Entity wallet = new WalletV2Entity();
        wallet.setId(walletId);
        return wallet;
    }
    
    @Named("mapToTransactionType")
    static TransactionType mapToTransactionType(String transactionType) {
        if (transactionType == null || transactionType.isEmpty()) {
            return null;
        }
        try {
            return TransactionType.valueOf(transactionType);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Type de transaction invalide: " + transactionType);
        }
    }
    
    static String mapStatusToString(TransactionStatus status) {
        return status != null ? status.name() : null;
    }
    
    static String mapTypeToString(TransactionType type) {
        return type != null ? type.name() : null;
    }
    
    static String mapTypeToLabel(TransactionType type) {
        return type != null ? type.getLabel() : null;
    }
    
    // ============================================================
    // QUALIFIED MAPPERS FOR COMPLEX CONVERSIONS
    // ============================================================
    
    @Named("mapStatusFromString")
    static TransactionStatus mapStatusFromString(String status) {
        if (status == null || status.isEmpty()) {
            return TransactionStatus.PENDING;
        }
        try {
            return TransactionStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            return TransactionStatus.PENDING;
        }
    }
    
    @Named("mapTypeFromString")
    static TransactionType mapTypeFromString(String type) {
        if (type == null || type.isEmpty()) {
            return null;
        }
        try {
            return TransactionType.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Type de transaction invalide: " + type);
        }
    }
    
    @Named("mapTimestampToLocalDateTime")
    static LocalDateTime mapTimestampToLocalDateTime(Long timestamp) {
        return timestamp != null ? LocalDateTime.now() : null; // Custom logic if needed
    }
    
    @Named("mapLocalDateTimeToTimestamp")
    static Long mapLocalDateTimeToTimestamp(LocalDateTime localDateTime) {
        return localDateTime != null ? System.currentTimeMillis() : null; // Custom logic if needed
    }
}