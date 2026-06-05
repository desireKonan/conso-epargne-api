package org.marketplace_lea.prometheus.domain.currency.form;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
public class CurrencySearchCriteria {
    private String code;          // recherche partielle
    private String label;         // recherche partielle
    private String description;   // recherche partielle

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate createdAtStart;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate createdAtEnd;
}