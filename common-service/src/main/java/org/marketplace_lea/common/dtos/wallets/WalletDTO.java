package org.marketplace_lea.common.dtos.wallets;

import lombok.*;
import org.marketplace_lea.common.dtos.AccountV2DTO;
import org.marketplace_lea.common.dtos.BaseDTO;
import org.marketplace_lea.common.entities.wallet.WalletV2Type;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WalletDTO extends BaseDTO {
    private String id;
    private String accountId;
    private String login;
    private String numberAndOperator;

    private WalletV2Type walletType;

    // Soldes principaux
    @Builder.Default
    BigDecimal balance = BigDecimal.ZERO;

    @Builder.Default
    BigDecimal totalAmountSend = BigDecimal.ZERO;

    @Builder.Default
    BigDecimal totalAmountReceived = BigDecimal.ZERO;

    @Builder.Default
    BigDecimal totalDrawnAmount = BigDecimal.ZERO;

    @Builder.Default
    BigDecimal reservedAmount = BigDecimal.ZERO;

    // Soldes d'investissement
    @Builder.Default
    BigDecimal investedCoinAmount = BigDecimal.ZERO;

    @Builder.Default
    BigDecimal investmentProfitsBalance = BigDecimal.ZERO;

    // Soldes de bonus
    @Builder.Default
    BigDecimal partnerBonusAmount = BigDecimal.ZERO;

    @Builder.Default
    BigDecimal specialBonusAmount = BigDecimal.ZERO;

    // Soldes d'épargne
    @Builder.Default
    BigDecimal personalSavingAmount = BigDecimal.ZERO;

    @Builder.Default
    BigDecimal firstLevelSavingAmount = BigDecimal.ZERO;

    @Builder.Default
    BigDecimal networkSavingAmount = BigDecimal.ZERO;

    // Autres soldes
    @Builder.Default
    BigDecimal voucherAmount = BigDecimal.ZERO;
}
