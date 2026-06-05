package org.marketplace_lea.common.dtos;

import lombok.Data;

@Data
public class PersonalWalletDTO extends WalletDTO {
    private float voucherAmount;

    private float partnerBonusAmount;

    private float personalSavingAmount;

    private float firstLevelSavingAmount;

    private float networkSavingAmount;

    private float specialBonusAmount;

    private float totalDrawnAmount;

    private float reservAmount;
}
