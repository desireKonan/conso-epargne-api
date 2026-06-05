package org.marketplace_lea.common.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.marketplace_lea.common.common.utils.GeneratorUtils;
import org.marketplace_lea.common.entities.account.AccountV2Entity;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "ce_voucher")
public class VoucherV2Entity {
    @Id
    private String id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "id_account")
    private AccountV2Entity account;

    @Column(name = "amount", nullable = false)
    private float amount;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    private boolean valid = true;

    @PrePersist
    public void prePersist() {
        id = GeneratorUtils.generateVoucherId();
        createdAt = LocalDateTime.now();
    }


    public boolean isNullOrValid() {
        return this.id == null || !this.valid;
    }
}
