package org.marketplace_lea.prometheus.domain.payment_method.specifications;

import jakarta.persistence.criteria.Predicate;
import org.marketplace_lea.prometheus.domain.payment_method.entities.PaymentMethodEntity;
import org.marketplace_lea.prometheus.domain.payment_method.form.PaymentMethodSearchCriteria;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PaymentMethodSpecifications {

    public static Specification<PaymentMethodEntity> byCriteria(PaymentMethodSearchCriteria criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(criteria.getLabel())) {
                predicates.add(cb.like(cb.lower(root.get("label")),
                        "%" + criteria.getLabel().toLowerCase() + "%"));
            }

            if (StringUtils.hasText(criteria.getProvider())) {
                predicates.add(cb.like(cb.lower(root.get("provider")),
                        "%" + criteria.getProvider().toLowerCase() + "%"));
            }

            if (criteria.getAvailable() != null) {
                predicates.add(cb.equal(root.get("available"), criteria.getAvailable()));
            }

            if (criteria.getOnline() != null) {
                predicates.add(cb.equal(root.get("online"), criteria.getOnline()));
            }

            if (criteria.getFeesMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("fees"), criteria.getFeesMin()));
            }

            if (criteria.getFeesMax() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("fees"), criteria.getFeesMax()));
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