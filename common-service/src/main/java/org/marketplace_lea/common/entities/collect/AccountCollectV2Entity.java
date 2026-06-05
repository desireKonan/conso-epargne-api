package org.marketplace_lea.common.entities.collect;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "AccountCollectV2")
@Table(name = "ce_account_collect_v2", indexes = {
        @Index(name = "idx_login", columnList = "login")
})
public class AccountCollectV2Entity {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @OneToOne(optional = false)
    @JoinColumn(name = "collect_id", nullable = false)
    private CollectV2Entity collect;

    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "login", nullable = false)
    private String login;

    @Column(name = "total_donation_amount", nullable = false)
    private BigDecimal totalDonationAmount;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt;

    @Column(name = "dismissed_at")
    private LocalDateTime dismissedAt;
}