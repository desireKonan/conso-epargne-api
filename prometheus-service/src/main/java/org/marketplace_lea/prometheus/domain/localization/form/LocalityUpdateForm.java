package org.marketplace_lea.prometheus.domain.localization.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocalityUpdateForm {
    @NotBlank(message = "Le label est obligatoire")
    private String label;

    @PositiveOrZero(message = "Les frais doivent être >= 0")
    private Float fees;
}