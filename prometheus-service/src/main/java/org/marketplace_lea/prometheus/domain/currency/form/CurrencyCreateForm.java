package org.marketplace_lea.prometheus.domain.currency.form;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyCreateForm {

    @NotBlank(message = "Le code devise est obligatoire")
    private String code;

    @NotBlank(message = "Le label est obligatoire")
    private String label;

    private String description;
}