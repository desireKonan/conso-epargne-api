package org.marketplace_lea.prometheus.domain.payment_method.form;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
public class PaymentMethodSearchCriteria {
    private String label;           // recherche partielle
    private String provider;        // recherche partielle
    private Boolean available;      // filtrer par disponibilité
    private Boolean online;         // filtrer par paiement en ligne
    private Float feesMin;          // frais minimum
    private Float feesMax;          // frais maximum

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate createdAtStart;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate createdAtEnd;
}