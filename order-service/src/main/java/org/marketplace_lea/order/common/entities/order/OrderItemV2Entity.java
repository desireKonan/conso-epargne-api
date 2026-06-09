package org.marketplace_lea.order.common.entities.order;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.marketplace_lea.common.entities.inventory.ProductV2Entity;

import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "OrderItemV2")
@Table(name = "ce_order_item_v2")
public class OrderItemV2Entity extends ItemV2Entity {
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "product_id")
    private ProductV2Entity product;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "order_id")
    private OrderV2Entity order;


    public float retrieveTotal() {
        return Optional.of(product)
                .map(ProductV2Entity::getPrice)
                .map(price -> price.floatValue() * getQuantity())
                .orElse(0.0f);
    }

    public double totalSaving() {
        return Optional.ofNullable(product)
                .filter(productV2 -> productV2.getSavingAmount() != null)
                .map(ProductV2Entity::getSavingAmount)
                .map(productV2 -> productV2.floatValue() * this.getQuantity())
                .orElse(0.0f);
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "product=" +
                product.getId() +
                ", " +
                product.getLabel() +
                ", " +
                product.getPrice() +
                ", order=" +
                order.getId() +
        '}';
    }
}