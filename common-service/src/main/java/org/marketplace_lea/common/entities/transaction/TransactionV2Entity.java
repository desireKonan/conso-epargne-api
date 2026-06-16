package org.marketplace_lea.common.entities.transaction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.ColumnDefault;
import org.marketplace_lea.common.entities.wallet.WalletV2Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.marketplace_lea.common.entities.transaction.TransactionType.COMMISSION;
import static org.marketplace_lea.common.entities.transaction.TransactionType.INVESTMENT_BONUS;
import static org.marketplace_lea.common.entities.transaction.TransactionType.LEA_AFFILIATION_PAYMENT;
import static org.marketplace_lea.common.entities.transaction.TransactionType.LEA_COIN_PAYMENT;
import static org.marketplace_lea.common.entities.transaction.TransactionType.LEA_COIN_TO_POINT_TRANSFERT;
import static org.marketplace_lea.common.entities.transaction.TransactionType.MEMBERSHIP_PAYMENT;
import static org.marketplace_lea.common.entities.transaction.TransactionType.NETWORK_PAYMENT;
import static org.marketplace_lea.common.entities.transaction.TransactionType.ORDER_PAYMENT;
import static org.marketplace_lea.common.entities.transaction.TransactionType.POINT_TO_LEA_COIN_TRANSFERT;
import static org.marketplace_lea.common.entities.transaction.TransactionType.PROFIT_TO_LEA_COIN_TRANSFERT;

@Slf4j
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "TransactionV2")
@Table(name = "ce_transaction_v2", indexes = {
        @Index(name = "idx_phone_number", columnList = "phone_number"),
        @Index(name = "idx_payment_reference", columnList = "payment_reference")
})
public class TransactionV2Entity {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "source_id", nullable = false)
    private WalletV2Entity source;

    @ManyToOne(optional = false)
    @JoinColumn(name = "destination_id", nullable = false)
    private WalletV2Entity destination;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "payment_reference", nullable = false)
    private String paymentReference;

    @Column(name = "object_id")
    private String objectId;

    @Column(name = "description")
    private String description;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "currency", nullable = false)
    private String currency;

    @Column(name = "coin_amount")
    private BigDecimal coinAmount;

    @ColumnDefault("0.0")
    @Column(name = "fees")
    private float fees;

    @Column(name = "provider", nullable = false)
    private String provider;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransactionStatus transactionStatus;

    @Column(name = "rejection_reason", nullable = false)
    private String rejectionReason;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TransactionType transactionType;

    @Column(name = "createdAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "validated_at")
    private LocalDateTime validatedAt;

    @Column(name = "failed_at")
    private LocalDateTime failedAt;

    public boolean hasPending() {
        return TransactionStatus.PENDING.equals(transactionStatus);
    }

    public boolean hasSuccess() {
        return TransactionStatus.SUCCESS.equals(transactionStatus);
    }

    public boolean hasFailed() {
        return TransactionStatus.FAILED.equals(transactionStatus);
    }

    public String validMessage() {
        return hasSuccess() ? "success" : "fail";
    }

    public boolean hasLeaCoinPayment() {
        return LEA_COIN_PAYMENT.equals(transactionType);
    }

    public boolean hasCommission() {
        return COMMISSION.equals(transactionType);
    }

    public boolean hasLeaToPointTransfert() {
        return LEA_COIN_TO_POINT_TRANSFERT.equals(transactionType);
    }

    public boolean hasPointToLeaTransfert() {
        return POINT_TO_LEA_COIN_TRANSFERT.equals(transactionType);
    }

    public boolean hasProfitsToLeaTransfert() {
        return PROFIT_TO_LEA_COIN_TRANSFERT.equals(transactionType);
    }

    public boolean hasNetworkPayment() {
        return NETWORK_PAYMENT.equals(transactionType);
    }

    public boolean hasMembershipPayment() {
        return MEMBERSHIP_PAYMENT.equals(transactionType);
    }

    public boolean hasLeaAffiliationPayment() {
        return LEA_AFFILIATION_PAYMENT.equals(transactionType);
    }

    public boolean hasInvestmentBonus() {
        return INVESTMENT_BONUS.equals(transactionType);
    }

    public boolean hasOrderPayment() {
        return ORDER_PAYMENT.equals(transactionType);
    }

    public boolean hasSameSource() {
        return source != null && destination != null && source.equals(destination);
    }

    public boolean hasLeaCoinTransaction() {
        return hasLeaCoinPayment() || hasPointToLeaTransfert() || hasProfitsToLeaTransfert();
    }


    public BigDecimal retrieveAmountToCredit() {
        return hasLeaCoinTransaction() ? this.getCoinAmount() : this.getAmount();
    }

    public BigDecimal retrieveAmountToRemoveFromSource() {
        return hasLeaToPointTransfert() ? getCoinAmount() : this.retrieveTotalAmount();
    }

    public BigDecimal retrieveTotalAmount() {
        return amount.add(BigDecimal.valueOf(fees));
    }

    public int retrieveIntTotalAmount() {
        return retrieveTotalAmount().intValue();
    }

    public int correctAmount() {
        return hasLeaCoinPayment() ? retrieveIntTotalAmount() : retrieveTotalAmount().intValue();
    }

    public BigDecimal retrieveAmountToDebit() {
        if (shouldRemoveAmountFromSource()) {
            return retrieveAmountToRemoveFromSource();
        }
        return BigDecimal.ZERO;
    }

    public boolean shouldDebitSource() {
        return !hasInvestmentBonus() && !hasProfitsToLeaTransfert() && source != null;
    }

    public boolean shouldCreditDestination() {
        return destination != null;
    }

    public void validate() {
        this.transactionStatus = TransactionStatus.SUCCESS;
        this.validatedAt = LocalDateTime.now();
    }

    public void invalidate() {
        if (this.rejectionReason == null) {
            rejectionReason = "Transaction annulée !";
        }
        this.transactionStatus = TransactionStatus.FAILED;
        this.failedAt = LocalDateTime.now();
    }

    private boolean hasLeaCoinToReverseTransaction() {
        return hasLeaCoinPayment() || hasPointToLeaTransfert() || hasProfitsToLeaTransfert();
    }

    // Permet de déterminer si l'on doit retirer le montant de la source en fonction du type de transaction
    private boolean shouldRemoveAmountFromSource() {
        return !hasInvestmentBonus() && !hasProfitsToLeaTransfert();
    }

    public void handleValidation(boolean eventHasSucceed) {
        if (eventHasSucceed) {
            this.validate();
        } else {
            this.invalidate();
        }
    }
}