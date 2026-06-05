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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.marketplace_lea.common.entities.wallet.WalletV2Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.marketplace_lea.common.entities.transaction.TransactionType.LEA_COIN_PAYMENT;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "TransactionV2")
@Table(name = "ce_transaction_v2", indexes = {
        @Index(name = "idx_phone_number", columnList = "phone_number"),
        @Index(name = "idx_payment_reference", columnList = "payment_reference")
})
public class TransactionV2Entity {
    @Id
    @Column(nullable = false)
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

    @Column(name = "fees")
    private float fees;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransactionStatus transactionStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TransactionType transactionType;

    @Column(name = "createdAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "validated_at")
    private LocalDateTime validatedAt;

    @Column(name = "failed_at")
    private LocalDateTime failedAt;


    public boolean hasLeaCoinPayment() {
        return LEA_COIN_PAYMENT.equals(transactionType);
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
}