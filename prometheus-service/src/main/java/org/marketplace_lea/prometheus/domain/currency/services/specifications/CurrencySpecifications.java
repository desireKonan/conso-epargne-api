package org.marketplace_lea.prometheus.domain.currency.services.specifications;

import jakarta.persistence.criteria.Predicate;
import org.marketplace_lea.common.entities.CurrencyV2Entity;
import org.marketplace_lea.prometheus.domain.currency.form.CurrencySearchCriteria;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CurrencySpecifications {

    public static Specification<CurrencyV2Entity> byCriteria(CurrencySearchCriteria criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(criteria.getCode())) {
                predicates.add(cb.like(cb.lower(root.get("code")),
                        "%" + criteria.getCode().toLowerCase() + "%"));
            }

            if (StringUtils.hasText(criteria.getLabel())) {
                predicates.add(cb.like(cb.lower(root.get("label")),
                        "%" + criteria.getLabel().toLowerCase() + "%"));
            }

            if (StringUtils.hasText(criteria.getDescription())) {
                predicates.add(cb.like(cb.lower(root.get("description")),
                        "%" + criteria.getDescription().toLowerCase() + "%"));
            }

            if (criteria.getCreatedAtStart() != null) {
                LocalDateTime start = criteria.getCreatedAtStart().atStartOfDay();
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), start));
            }

            if (criteria.getCreatedAtEnd() != null) {
                LocalDateTime end = criteria.getCreatedAtEnd().atTime(23, 59, 59);
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), end));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}