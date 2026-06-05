package org.marketplace_lea.prometheus.domain.localization.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistrictDto {
    private String id;
    private String localityId;      // identifiant de la localité parente
    private String localityLabel;   // nom de la localité (optionnel, pour affichage)
    private String label;
    private Float fees;              // dérivé de la localité (lecture seule)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}