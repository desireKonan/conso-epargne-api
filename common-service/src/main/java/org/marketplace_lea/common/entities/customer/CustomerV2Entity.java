package org.marketplace_lea.common.entities.customer;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.marketplace_lea.common.common.utils.GeneratorUtils;
import org.marketplace_lea.common.entities.account.AccountV2Entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "CustomerV2")
@Table(name = "ce_customer_v2", indexes = {
        @Index(name = "idx_email", columnList = "email")
})
public class CustomerV2Entity {
    @Id
    @Column(nullable = false, length = 40)
    private String id;

    @JoinColumn(name = "account_id", nullable = false)
    @OneToOne(optional = false, cascade = CascadeType.ALL)
    private AccountV2Entity account;

    @Column(name = "firstname", nullable = false)
    private String firstName;

    @Column(name = "lastname", nullable = false)
    private String lastName;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone_numbers", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> phoneNumbers = new ArrayList<>();

    @Column(name = "address")
    private String address;

    @Column(name = "longitude", columnDefinition = "DECIMAL(10, 8)")
    private BigDecimal longitude;

    @Column(name = "latitude", columnDefinition = "DECIMAL(10, 8)")
    private BigDecimal latitude;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "verified_at")
    private LocalDateTime verfiedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @PrePersist
    public void prePersist() {
        id = GeneratorUtils.generateCustomerId();
        createdAt = LocalDateTime.now();
    }

    public String _fullname() {
        if (account.hasSystemType()) {
            return "SYSTEM";
        }

        return String.format("%s %s", this.getLastName(), this.getFirstName());
    }

    public String reduceFullNameAndNumber() {
        if (this.getFirstName() != null && this.getLastName() != null) {
            return String.format("%s %s(%s)", this.getFirstName().split(" ")[0], this.getLastName().toUpperCase(),
                    account.getLogin());
        } else if (this.getFirstName() != null) {
            return String.format("%s (%s)", this.getFirstName().split(" ")[0], account.getLogin());
        } else if (this.getLastName() != null) {
            return String.format("%s (%s)", this.getLastName().split(" ")[0], account.getLogin());
        } else
            return "Non renseigné";
    }

    @Transient
    public String email() {
        return Optional.of(this)
                .map(CustomerV2Entity::getEmail)
                .orElse("Aucun");
    }

    @Transient
    @JsonIgnore
    public String fullname() {
        return String.format("%s %s", lastName.split(" ")[0], firstName.split(" ")[0]);
    }

    public String login() {
        return Optional.ofNullable(account)
                .map(AccountV2Entity::getLogin)
                .orElse("Aucun");
    }

    public String accountId() {
        return Optional.ofNullable(account)
                .map(AccountV2Entity::getId)
                .orElse("Aucun");
    }

    public boolean accountBlocked() {
        return account != null && account.isBlacklisted();
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", account=" + account.getId() +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
