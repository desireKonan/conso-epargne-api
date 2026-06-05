package org.marketplace_lea.application.configuration.data_initializer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.marketplace_lea.common.entities.parameters.ParameterType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParameterConfigs {
    private String key;
    private String value;
    private String description;
    private ParameterType dataType;
}
