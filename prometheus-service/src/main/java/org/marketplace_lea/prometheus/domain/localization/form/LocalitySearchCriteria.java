package org.marketplace_lea.prometheus.domain.localization.form;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
public class LocalitySearchCriteria {
    private String label;         // recherche partielle
    private Float feesMin;        // frais minimum
    private Float feesMax;        // frais maximum
    private Boolean deleted;      // filtrer les localités supprimées ou non

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate createdAtStart;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate createdAtEnd;
}