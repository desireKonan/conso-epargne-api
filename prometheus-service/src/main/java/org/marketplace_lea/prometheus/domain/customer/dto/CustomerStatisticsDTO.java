package org.marketplace_lea.prometheus.domain.customer.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO représentant les statistiques d'un niveau donné dans le réseau.
 *
 * <p>
 * Contient les points générés, le pourcentage de commission,
 * le montant des commissions et le nombre d'utilisateurs
 * (consomm'acteurs vs. partenaires).
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerStatisticsDTO {
    private float generatedPoints;
    private float commissionRate;
    private float commissions;
    private int totalUsersCount;
    private int customerCount;
    private int partnerCount;
}
