package org.marketplace_lea.common.dtos;

import lombok.Data;

@Data
public class LeaWalletDTO extends WalletDTO {
    private float investedCoinAmount;
    private float totalCoinAmount;
    private float investmentProfitsBalance;
    private float reservAmount;
}
