package org.marketplace_lea.prometheus.domain.parameter_config.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParameterConfigSearchCriteria {
    private String key;          // recherche partielle (LIKE)
    private String value;        // recherche partielle
    private String dataType;     // égalité exacte (STRING, BOOLEAN, INT, FLOAT)

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate createdAtStart;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate createdAtEnd;
}