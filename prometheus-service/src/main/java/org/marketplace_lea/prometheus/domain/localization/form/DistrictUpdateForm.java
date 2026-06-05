package org.marketplace_lea.prometheus.domain.localization.form;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistrictUpdateForm {
    @NotBlank(message = "L'identifiant de la localité est obligatoire")
    private String localityId;

    @NotBlank(message = "Le label du district est obligatoire")
    private String label;
}