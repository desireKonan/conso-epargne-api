package org.marketplace_lea.common.entities.wallet;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.marketplace_lea.common.entities.BaseEntity;
import org.marketplace_lea.common.entities.account.AccountV2Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.marketplace_lea.common.common.utils.GeneratorUtils.generateWalletId;


@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "WalletV2")
@Table(name = "ce_wallet_v2", indexes = {
        @Index(name = "idx_wallet_type", columnList = "wallet_type")
})
public class WalletV2Entity extends BaseEntity {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private AccountV2Entity account;

    @Column(name = "number_operator", nullable = false)
    private String numberAndOperator;

    @Column(name = "devise", nullable = false)
    private String devise;

    @Column(name = "balance", columnDefinition = "DECIMAL(10, 4)", nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "total_amount_send", columnDefinition = "DECIMAL(10, 4)")
    private BigDecimal totalAmountSend = BigDecimal.ZERO;

    @Column(name = "total_amount_received", columnDefinition = "DECIMAL(10, 4)")
    private BigDecimal totalAmountReceived = BigDecimal.ZERO;

    @Column(name = "invested_coin_amount", columnDefinition = "DECIMAL(10, 4)")
    private BigDecimal investedCoinAmount = BigDecimal.ZERO;

    @Column(name = "partner_bonus_amount", columnDefinition = "DECIMAL(10, 4)")
    private BigDecimal partnerBonusAmount = BigDecimal.ZERO;

    @Column(name = "personal_saving_amount", columnDefinition = "DECIMAL(10, 4)")
    private BigDecimal personalSavingAmount = BigDecimal.ZERO;

    @Column(name = "first_level_saving_amount", columnDefinition = "DECIMAL(10, 4)")
    private BigDecimal firstLevelSavingAmount = BigDecimal.ZERO;

    @Column(name = "network_saving_amount", columnDefinition = "DECIMAL(10, 4)")
    private BigDecimal networkSavingAmount = BigDecimal.ZERO;

    @Column(name = "special_bonus_amount", columnDefinition = "DECIMAL(10, 4)")
    private BigDecimal specialBonusAmount = BigDecimal.ZERO;

    @Column(name = "voucher_amount", columnDefinition = "DECIMAL(10, 4)")
    private BigDecimal voucherAmount = BigDecimal.ZERO;

    @Column(name = "total_drawn_amount", columnDefinition = "DECIMAL(10, 4)")
    private BigDecimal totalDrawnAmount = BigDecimal.ZERO;

    @Column(name = "reserved_amount", columnDefinition = "DECIMAL(10, 4)")
    private BigDecimal reservedAmount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "wallet_type", nullable = false)
    private WalletV2Type walletType;

    @PrePersist
    public void prePersist() {
        id = generateWalletId();
        super.setCreatedAt(LocalDateTime.now());
    }
}
