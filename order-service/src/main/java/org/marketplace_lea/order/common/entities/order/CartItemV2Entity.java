package org.marketplace_lea.order.common.entities.order;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.marketplace_lea.common.entities.customer.CustomerV2Entity;
import org.marketplace_lea.order.common.entities.inventory.ProductV2Entity;

import java.math.BigDecimal;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "CartItemV2")
@Table(name = "ce_cart_item")
public class CartItemV2Entity extends ItemV2Entity {
    @JsonBackReference
    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    private ProductV2Entity product;

    @JsonBackReference
    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id")
    private CustomerV2Entity customer;

    @Enumerated(EnumType.STRING)
    @Column(name = "cart_type", nullable = false)
    private CartType cartType;

    public String productId() {
        return Optional.ofNullable(product)
                .map(ProductV2Entity::getId)
                .orElse("EMPTY");
    }

    public BigDecimal claimProductPrice() {
        return Optional.ofNullable(super.getPrice())
                .map(price -> price.multiply(BigDecimal.valueOf(super.getQuantity())))
                .orElse(BigDecimal.ZERO);
    }

    public BigDecimal claimProductSavingAmount() {
        BigDecimal savingAmount = BigDecimal.ZERO;
        if (super.getSavingAmount() != null) {
            savingAmount = super.getSavingAmount()
                    .multiply(BigDecimal.valueOf(getQuantity()));
        }
        return savingAmount;
    }

    public boolean customerActive() {
        return customer != null && !customer.accountBlocked();
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + getId() +
                "product=" + product +
                ", customer=" + customer +
        '}';
    }
}