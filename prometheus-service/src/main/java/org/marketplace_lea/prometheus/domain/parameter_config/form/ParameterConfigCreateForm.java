package org.marketplace_lea.prometheus.domain.parameter_config.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.marketplace_lea.common.entities.parameters.ParameterType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParameterConfigCreateForm {

    @NotBlank(message = "La clé est obligatoire")
    private String key;

    @NotBlank(message = "La valeur est obligatoire")
    private String value;

    private String description;

    @NotNull(message = "Le type de données est obligatoire")
    private ParameterType dataType;
}