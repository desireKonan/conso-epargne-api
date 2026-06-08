package org.marketplace_lea.order.common.entities.inventory;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.marketplace_lea.common.common.utils.GeneratorUtils;
import org.marketplace_lea.common.entities.BaseEntity;

import static org.marketplace_lea.common.common.utils.MediaUtils.getShelfImageUrl;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ShelfV2")
@Table(name = "ce_shelf_v2")
public class ShelfV2Entity extends BaseEntity {
    @Id
    @Column(nullable = false, length = 40)
    private String id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "parent_id")
    private ShelfV2Entity parent;

    @JsonIgnore
    @Column(name = "ce_rank")
    private int rank = 0;

    @JsonBackReference
    @ManyToOne(optional = false)
    @JoinColumn(name = "ensign_id", nullable = false)
    private EnsignV2Entity ensign;

    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "image", nullable = false)
    private String image;

    @PrePersist
    public void prePersist() {
        id = GeneratorUtils.generateShelfId();
    }

    @Transient
    public String getImageUrl() {
        if (image == null) return null;
        return getShelfImageUrl(image);
    }

    @Override
    public String toString() {
        return "Shelf{" +
                "id='" + id + '\'' +
                ", label='" + label + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", createdAt=" + super.getCreatedAt() +
        '}';
    }
}
