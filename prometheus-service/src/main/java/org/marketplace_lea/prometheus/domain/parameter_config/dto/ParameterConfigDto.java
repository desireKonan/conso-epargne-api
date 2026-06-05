package org.marketplace_lea.prometheus.domain.parameter_config.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParameterConfigDto {
    private Long id;
    private String key;
    private String value;
    private String description;
    private String dataType;   // STRING, BOOLEAN, INT, FLOAT
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}