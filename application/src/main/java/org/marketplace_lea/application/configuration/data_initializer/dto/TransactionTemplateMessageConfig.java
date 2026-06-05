package org.marketplace_lea.application.configuration.data_initializer.dto;

import org.marketplace_lea.common.entities.transaction.TransactionType;

import java.util.Map;

public record TransactionTemplateMessageConfig(
        String id,
        Map<String, String> titles,
        Map<String, String> messages,
        TransactionType transactionType
) {
}
