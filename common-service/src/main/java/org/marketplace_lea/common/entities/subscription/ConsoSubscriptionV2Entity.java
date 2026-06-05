package org.marketplace_lea.common.entities.subscription;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.marketplace_lea.common.entities.customer.CustomerV2Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Subscription")
@Table(name = "ce_subscription")
@SQLDelete(sql = "UPDATE ce_subscription SET deleted_at = now() WHERE id = ?")
public class ConsoSubscriptionV2Entity {
    @Id
    @Column(nullable = false, length = 40)
    private String id;

    @Column(name = "type")
    private String type = ConsoSubscriptionType.SILVER.value();

    @Column(name = "amount")
    private BigDecimal amount;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "customer_id")
    private CustomerV2Entity customer;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "active")
    private Boolean active = true; // Ajout Orlando
}
