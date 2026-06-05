package org.marketplace_lea.prometheus.domain.template_messages.services.specifications;

import jakarta.persistence.criteria.Predicate;
import org.marketplace_lea.common.entities.transaction.TransactionNotificationTemplateEntity;
import org.marketplace_lea.prometheus.domain.template_messages.form.NotificationTemplateSearchCriteria;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionNotificationTemplateSpecifications {

    public static Specification<TransactionNotificationTemplateEntity> byCriteria(NotificationTemplateSearchCriteria criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. Recherche partielle sur la clé (insensible à la casse)
            if (StringUtils.hasText(criteria.getKey())) {
                predicates.add(cb.like(cb.lower(root.get("key")), "%" + criteria.getKey().toLowerCase() + "%"));
            }

            // 2. Type de transaction exact
            if (criteria.getTransactionType() != null) {
                predicates.add(cb.equal(root.get("transactionType"), criteria.getTransactionType()));
            }

            // 3. Active (si spécifié)
            if (criteria.getActive() != null) {
                predicates.add(cb.equal(root.get("active"), criteria.getActive()));
            }

            // 4. Date de début (createdAt >= start)
            if (criteria.getCreatedAtStart() != null) {
                LocalDateTime start = criteria.getCreatedAtStart().atStartOfDay();
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), start));
            }

            // 5. Date de fin (createdAt <= end)
            if (criteria.getCreatedAtEnd() != null) {
                LocalDateTime end = criteria.getCreatedAtEnd().atTime(23, 59, 59);
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), end));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}