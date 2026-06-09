package org.marketplace_lea.prometheus.domain.payment_method.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;import org.marketplace_lea.common.entities.BaseEntity;

import static org.marketplace_lea.common.common.utils.MediaUtils.getPaymentMethodImageUrl;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "PaymentMethod")
@Table(name = "ce_payment_method")
public class PaymentMethodEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "label", nullable = false)
    private String label;

    @ColumnDefault("true")
    @Column(name = "available", nullable = false)
    private boolean available;

    @ColumnDefault("false")
    @Column(name = "online", nullable = false)
    private boolean online;

    @Column(name = "provider", nullable = false)
    private String provider;

    @JsonIgnore
    @Column(name = "image_url")
    private String image;

    @ColumnDefault("0.0")
    @Column(name = "fees")
    private float fees;

    @Transient
    public String getImageUrl() {
        if (image == null) {
            return null;
        }
        return getPaymentMethodImageUrl(image);
    }

    @JsonIgnore
    public boolean isWave() {
        return provider != null && provider.trim()
                .replace(" ", "")
                .equalsIgnoreCase("WAVE");
    }


    public boolean isMobileMoneyPayment() {
        return isWave() || isOnline();
    }
}