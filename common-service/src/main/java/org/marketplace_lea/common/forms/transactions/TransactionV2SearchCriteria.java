package org.marketplace_lea.common.forms.transactions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.marketplace_lea.common.entities.transaction.TransactionStatus;
import org.marketplace_lea.common.entities.transaction.TransactionType;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionV2SearchCriteria {
    private String sourceWalletId;

    private String destinationWalletId;

    private String phoneNumber;

    private String paymentReference;

    private String objectId;

    private TransactionStatus transactionStatus;

    private TransactionType transactionType;

    private String currency;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDate;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endDate;
    
    private BigDecimal minAmount;

    private BigDecimal maxAmount;
}