package org.marketplace_lea.prometheus.domain.customer.dto;

import org.marketplace_lea.common.dtos.AccountV2DTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO représentant le profil complet du client connecté.
 *
 * <p>
 * Agrège les informations personnelles du client,
 * les informations de son compte et ses coordonnées.
 * Utilisé par l'endpoint {@code GET /me}.
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerProfileDTO {
    private String id;
    private AccountV2DTO account;
    private String firstName;
    private String lastName;
    private String email;
    private List<String> phoneNumbers = new ArrayList<>();
    private String address;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
