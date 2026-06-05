package org.marketplace_lea.common.entities.collect;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import org.marketplace_lea.common.entities.BaseEntity;
import org.marketplace_lea.common.entities.ProjectTypeV2Entity;
import org.marketplace_lea.common.entities.account.AccountV2Entity;
import org.marketplace_lea.common.entities.wallet.WalletV2Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;


@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "CollectV2")
@Table(name = "ce_collect_v2", indexes = {
        @Index(name = "idx_code", columnList = "code")
})
public class CollectV2Entity extends BaseEntity {
    @Id
    @Column(nullable = false, length = 40)
    private String id;

    @OneToOne(optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private AccountV2Entity account;

    @OneToOne(optional = false)
    @JoinColumn(name = "wallet_id", nullable = false)
    private WalletV2Entity walletV2Entity;

    @OneToOne(optional = false)
    @JoinColumn(name = "project_type_id", nullable = false)
    private ProjectTypeV2Entity projectType;

    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Column(name = "description")
    private String description;

    @Column(name = "counterParty", nullable = false)
    private float counterParty;

    @Column(name = "min_members", nullable = false)
    private int minMembers;

    @Column(name = "reach_amount", nullable = false)
    private BigDecimal reachAmount;

    @Column(name = "duration", nullable = false)
    private int duration;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CollectStatus status;

    @Column(name = "partner_only", nullable = false)
    private boolean partnerOnly;

    @Column(name = "added_member_only", nullable = false)
    private boolean addedMemberOnly;

    @Column(name = "image", nullable = false)
    private String image;

    @Column(name = "videoUrl", nullable = false)
    private String videoUrl;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
}
