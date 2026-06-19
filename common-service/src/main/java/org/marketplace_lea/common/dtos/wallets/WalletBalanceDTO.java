package org.marketplace_lea.common.dtos.wallets;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletBalanceDTO {
    private String walletId;
    private BigDecimal investedCoinAmount;
    private BigDecimal balance;
    private String accountId;
    private String accountLogin;
    private String numberAndOperator;
}



