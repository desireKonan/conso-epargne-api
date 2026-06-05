package org.marketplace_lea.common.dtos;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsomWalletDTO {
    private String id;
    private BigDecimal investedCoinAmount;
    private BigDecimal balance;
    private String accountId;
    private BigDecimal reservAmount;
}
