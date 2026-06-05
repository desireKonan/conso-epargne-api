package org.marketplace_lea.common.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Country")
@Table(name = "ce_country")
public class CountryV2Entity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "label")
    private String label;

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "calling_code", unique = true)
    private String callingCode;

    @Column(name = "phone_number_length")
    private int phoneNumberLength;

    @Column(name = "enabled")
    private boolean enabled = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "disabled_at")
    private LocalDateTime disabledAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}
