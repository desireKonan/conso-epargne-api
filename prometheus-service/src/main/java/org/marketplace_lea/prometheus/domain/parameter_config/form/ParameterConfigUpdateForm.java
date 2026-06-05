package org.marketplace_lea.prometheus.domain.parameter_config.form;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.marketplace_lea.common.entities.parameters.ParameterType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParameterConfigUpdateForm {

    @NotBlank(message = "La clé est obligatoire")
    private String key;

    @NotBlank(message = "La valeur est obligatoire")
    private String value;

    private String description;

    private ParameterType dataType;
}