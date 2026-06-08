package org.marketplace_lea.order.common.entities.inventory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.marketplace_lea.common.common.utils.GeneratorUtils;
import org.marketplace_lea.common.entities.BaseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.marketplace_lea.common.common.utils.MediaUtils.getProductImageUrl;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ProductV2")
@Table(name = "ce_product_v2")
public class ProductV2Entity extends BaseEntity {
    @Id
    @Column(nullable = false, length = 40)
    private String id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "shelf_id", nullable = false)
    private ShelfV2Entity shelf;

    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @JsonIgnore
    @Column(name = "ce_rank")
    private int rank;

    @Column(name = "weight")
    private double weight;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "purchase_price", nullable = false, columnDefinition = "DECIMAL(10, 8)")
    private BigDecimal purchasePrice = BigDecimal.ZERO;

    @Column(name = "conso_epargne_margin", nullable = false, columnDefinition = "DECIMAL(10, 8)")
    private BigDecimal consoEpargneMargin = BigDecimal.ZERO;

    @Column(name = "conso_account_per_amount", nullable = false, columnDefinition = "DECIMAL(10, 8)")
    private BigDecimal comissionAccountAmount = BigDecimal.ZERO;

    @Column(name = "price", nullable = false, columnDefinition = "DECIMAL(10, 8)")
    private BigDecimal price;

    @Column(name = "saving_amount", nullable = false, columnDefinition = "DECIMAL(10, 8)")
    private BigDecimal savingAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ProductType type;

    @Column(name = "image")
    private String image;

    @Column(name = "sold_out")
    private boolean soldOut;

    @ColumnDefault("false")
    @Column(name = "active_cashback")
    private boolean activeCashback;

    @Column(name = "published_at", nullable = false)
    private LocalDateTime publishedAt;

    public String imageUrl() {
        if (image == null) {
            return null;
        }
        return getProductImageUrl(id, image);
    }

    @PrePersist
    public void prePersist() {
        id = GeneratorUtils.generateProductId();
    }
}