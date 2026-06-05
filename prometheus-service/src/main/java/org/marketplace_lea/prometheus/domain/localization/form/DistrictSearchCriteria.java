package org.marketplace_lea.prometheus.domain.localization.form;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
public class DistrictSearchCriteria {
    private String label;               // recherche partielle
    private String localityId;          // égalité exacte
    private Boolean includeDeleted;     // si true, inclut les districts supprimés (default false)

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate createdAtStart;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate createdAtEnd;
}