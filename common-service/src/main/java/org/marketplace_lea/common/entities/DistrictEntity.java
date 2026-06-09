package org.marketplace_lea.common.entities;

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
import org.marketplace_lea.common.common.utils.GeneratorUtils;

import java.math.BigDecimal;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "District")
@Table(name = "ce_district")
public class DistrictEntity extends BaseEntity {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "locality_id")
    private LocalityEntity locality;

    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "longitude", columnDefinition = "DECIMAL(10, 8)")
    private BigDecimal longitude;

    @Column(name = "latitude", columnDefinition = "DECIMAL(10, 8)")
    private BigDecimal latitude;

    @PrePersist
    public void prePersist() {
        id = GeneratorUtils.generateDistrictId();
    }

    public float getFees() {
        return Optional.ofNullable(locality)
                .map(LocalityEntity::getFees)
                .orElse(0.0f);
    }
}
