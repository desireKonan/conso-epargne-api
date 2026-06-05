package org.marketplace_lea.common.entities.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.marketplace_lea.common.entities.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "AccountTypeV2")
@Table(name = "ce_account_type_v2")
public class AccountTypeV2Entity extends BaseEntity {
    @Id
    @Column(nullable = false)
    private String id;

    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "fees")
    private double fees;

    @Column(name = "withdrawal_fees")
    private float withdrawalFees;

    @Column(name = "voucher_fees")
    private float voucherFees;

    @JsonIgnore
    @Column(name = "voucher_bonus_amount")
    private double voucherBonusAmount;

    @JsonIgnore
    @Column(name = "partner_bonus_amount")
    private double partnerBonusAmount;
}
