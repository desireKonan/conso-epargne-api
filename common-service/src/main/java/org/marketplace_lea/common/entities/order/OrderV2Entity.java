package org.marketplace_lea.common.entities.order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import org.marketplace_lea.common.entities.VoucherV2Entity;
import org.marketplace_lea.common.entities.account.AccountV2Entity;
import org.marketplace_lea.common.entities.customer.CustomerV2Entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.marketplace_lea.common.entities.order.OrderStatus.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "OrderV2")
@Table(name = "ce_order_detail_v2")
public class OrderV2Entity {
    @Id
    @Column(name = "id", nullable = false, length = 40)
    private String id;

    @JsonBackReference
    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerV2Entity customer;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "voucher_id")
    private VoucherV2Entity voucher;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    // Pour latitude : DECIMAL(10, 8)
    @Column(name = "latitude", columnDefinition = "DECIMAL(10, 8)")
    private BigDecimal latitude;

    // Pour longitude : DECIMAL(11, 8)
    @Column(name = "longitude", columnDefinition = "DECIMAL(11, 8)")
    private BigDecimal longitude;

    @ColumnDefault("0.0")
    @Column(name = "total_amount", nullable = false)
    private float totalAmount;

    @ColumnDefault("0.0")
    @Column(name = "fees", nullable = false)
    private float fees;

    @ColumnDefault("0.0")
    @Column(name = "online_fees")
    private float onlineFees;

    @ColumnDefault("0.0")
    @Column(name = "delivery_fees", nullable = false)
    private float deliveryFees;

    @ColumnDefault("false")
    @Column(name = "delivered", nullable = false)
    private boolean delivered;

    @ColumnDefault("'pending'")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status = PENDING;

    @ColumnDefault("false")
    @Column(name = "has_online_payment", nullable = false)
    private boolean hasOnlinePayment;

    @Column(name = "provider", nullable = false)
    private String provider;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "validated_at")
    private LocalDateTime validatedAt;

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @JsonManagedReference
    @OneToMany(mappedBy = "order")
    private Set<OrderItemV2Entity> orderItems = new HashSet<>();

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    @Transient
    public Double getTotalOrderPrice() {
        double sum = 0D;
        for (OrderItemV2Entity oi : getOrderItems()) {
            sum += oi.total();
        }
        return sum;
    }

    @JsonIgnore
    public boolean isPending() {
        return PENDING.equals(status);
    }

    @JsonIgnore
    public boolean isValidated() {
        return VALIDATED.equals(status);
    }

    @JsonIgnore
    public boolean isCanceled() {
        return CANCELED.equals(status);
    }

    @JsonIgnore
    public String statusLabel() {
        return Optional.ofNullable(status)
                .map(OrderStatus::getLabel)
                .orElse("Non définit !");
    }

    @Transient
    public Double getTotalSavingAmount() {
        double sum = 0D;
        for (OrderItemV2Entity oi : getOrderItems()) {
            sum += oi.totalSaving();
        }
        return sum;
    }

    public AccountV2Entity account() {
        return Optional.ofNullable(customer)
                .map(CustomerV2Entity::getAccount)
                .orElse(null);
    }



    public void validate() {
        status = VALIDATED;
    }

    @Override
    public String toString() {
        return "Order{" +
                "status=" + status +
                ", total=" + totalAmount +
                ", customer=" + customer.getId() +
                ", paymentDetail=" + provider +
                ", orderItems=" + orderItems +
                '}';
    }
}