package org.marketplace_lea.common.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.marketplace_lea.common.common.utils.GeneratorUtils;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Locality")
@Table(name = "ce_locality")
public class LocalityEntity extends BaseEntity {
    @Id
    @Column(nullable = false, length = 40)
    private String id;

    @Column(name = "label", unique = true, nullable = false)
    private String label;

    @ColumnDefault("0")
    @Column(name = "fees")
    private float fees;

    @Column(name = "longitude", columnDefinition = "DECIMAL(10, 8)")
    private BigDecimal longitude;

    @Column(name = "latitude", columnDefinition = "DECIMAL(10, 8)")
    private BigDecimal latitude;

    @PrePersist
    public void prePersist() {
        id = GeneratorUtils.generateLocalityId();
    }

    @Override
    public String toString() {
        return "Locality{" +
                "id='" + id + '\'' +
                ", label='" + label + '\'' +
                '}';
    }
}
