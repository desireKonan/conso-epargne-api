package org.marketplace_lea.common.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ce_devise")
public class CurrencyV2Entity extends BaseEntity {
    @Id
    @Column(nullable = false)
    private String code;

    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}
