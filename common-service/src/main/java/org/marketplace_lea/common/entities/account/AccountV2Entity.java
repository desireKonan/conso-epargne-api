package org.marketplace_lea.common.entities.account;

import org.marketplace_lea.common.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static org.marketplace_lea.common.common.constants.ConsoEpargneConstants.*;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "AccountV2")
@Table(name = "ce_account_v2", indexes = {
        @Index(name = "idx_login", columnList = "login"),
        @Index(name = "idx_affiliation_code", columnList = "affiliation_code")
})
public class AccountV2Entity extends BaseEntity {
    @Id
    @Column(nullable = false, length = 40)
    private String id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "account_type_id", nullable = false)
    private AccountTypeV2Entity accountType;

    @Column(name = "affiliation_code", unique = true)
    private String affiliationCode;

    @Column(name = "country_code", nullable = false)
    private String countryCode = "CIV";

    @Column(unique = true, name = "login", nullable = false)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "notification_token", columnDefinition = "TEXT")
    private String notificationToken;

    @Column(name = "blacklisted")
    private boolean blacklisted = false;

    public boolean bePartner() {
        return accountType != null && PARTNER.equals(accountType.getId());
    }

    public boolean beCustomerType() {
        return accountType != null && CUSTOMER.equals(accountType.getId());
    }

    public boolean hasSystemType() {
        return accountType != null && SYSTEM_ACCOUNT_ID.equals(accountType.getId());
    }

    public boolean hasActive() {
        return !blacklisted;
    }

    public void updateLogin(String login) {
        this.login = login
                .replace(" ", "")
                .trim();
    }


    @Override
    public String toString() {
        return "Account{" +
                "login='" + login + '\'' +
                '}';
    }
}
