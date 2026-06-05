package org.marketplace_lea.prometheus.domain.template_messages.form;

import lombok.Data;
import org.marketplace_lea.common.entities.transaction.TransactionType;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
public class NotificationTemplateSearchCriteria {
    private String key;                     // recherche partielle (LIKE)
    private TransactionType transactionType;
    private Boolean active;                 // true / false / null (indifférent)

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate createdAtStart;       // date de création à partir de (inclusive)

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate createdAtEnd;         // date de création jusqu'à (inclusive)
}