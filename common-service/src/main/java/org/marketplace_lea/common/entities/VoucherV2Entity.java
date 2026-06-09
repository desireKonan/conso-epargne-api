package org.marketplace_lea.common.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.marketplace_lea.common.common.utils.GeneratorUtils;
import org.marketplace_lea.common.entities.account.AccountV2Entity;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Voucher")
@Table(name = "ce_voucher")
public class VoucherV2Entity {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @JsonBackReference
    @ManyToOne(optional = false)
    @JoinColumn(name = "account_id")
    private AccountV2Entity account;

    @Column(name = "amount", nullable = false)
    private float amount;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @ColumnDefault("true")
    @Column(name = "valid", nullable = false)
    private boolean valid;

    @PrePersist
    public void prePersist() {
        id = GeneratorUtils.generateVoucherId();
        createdAt = LocalDateTime.now();
    }


    public boolean isNullOrValid() {
        return this.id == null || !this.valid;
    }
}
