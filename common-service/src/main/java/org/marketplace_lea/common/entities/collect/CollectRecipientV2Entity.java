package org.marketplace_lea.common.entities.collect;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "CollectRecipientV2")
@Table(name = "ce_colllect_recipient_v2", indexes = {
        @Index(name = "idx_phone_number", columnList = "phone_number")
})
public class CollectRecipientV2Entity {
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

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @ColumnDefault("true")
    @Column(name = "company_member", nullable = false)
    private boolean companyMember;

    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt;

    @Column(name = "dimissed_at", nullable = false)
    private LocalDateTime dismissedAt;
}