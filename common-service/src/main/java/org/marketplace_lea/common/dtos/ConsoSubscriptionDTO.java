package org.marketplace_lea.common.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsoSubscriptionDTO {
    private String id;
    private String type;
    private BigDecimal amount;
    private String customerId;
    private String customerName; // Optionnel, si vous voulez inclure le nom du client
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Boolean active; // Ajout Orlando
}
