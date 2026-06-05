package org.marketplace_lea.prometheus.domain.localization.form;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
public class CountrySearchCriteria {
    private String label;          // recherche partielle
    private String code;           // égalité exacte
    private String callingCode;    // égalité exacte
    private Boolean enabled;       // filtre actif/inactif

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate createdAtStart;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate createdAtEnd;
}