package org.marketplace_lea.common.dtos.transactions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.marketplace_lea.common.dtos.wallets.WalletBalanceDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private String id;

    private WalletBalanceDTO source;

    private WalletBalanceDTO destination;

    private String phoneNumber;

    private String paymentReference;

    private String objectId;

    private String description;

    private BigDecimal amount;

    private String currency;

    private BigDecimal coinAmount;

    private Float fees;

    private String transactionStatus;

    private String transactionTypeLabel;

    private String transactionType;

    private LocalDateTime createdAt;

    private LocalDateTime validatedAt;

    private LocalDateTime failedAt;

    private BigDecimal totalAmount;
}
