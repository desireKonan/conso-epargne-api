package org.marketplace_lea.order.common.entities.inventory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.marketplace_lea.common.common.utils.GeneratorUtils;
import org.marketplace_lea.common.entities.BaseEntity;

import java.time.LocalDateTime;

import static org.marketplace_lea.common.common.utils.MediaUtils.getEnsignImageUrl;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "EnsignV2")
@Table(name = "ce_ensign_v2")
public class EnsignV2Entity extends BaseEntity {
    @Id
    @Column(nullable = false, length = 40)
    private String id;

    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "description", nullable = false)
    private String description;

    @JsonIgnore
    @Column(name = "ce_rank", nullable = false)
    @Generated(GenerationTime.INSERT)
    private int rank = 0;

    @Column(name = "image")
    private String image;

    @ColumnDefault("true")
    @Column(name = "available", nullable = false)
    private boolean available;

    @PrePersist
    public void prePersist() {
        id = GeneratorUtils.generateEnsignId();
        super.setCreatedAt(LocalDateTime.now());
    }

    @Transient
    public String getImageUrl() {
        if (image == null) return null;
        return getEnsignImageUrl(image);
    }
}
