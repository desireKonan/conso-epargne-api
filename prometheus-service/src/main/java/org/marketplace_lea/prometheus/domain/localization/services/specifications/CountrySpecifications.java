package org.marketplace_lea.prometheus.domain.localization.services.specifications;

import jakarta.persistence.criteria.Predicate;
import org.marketplace_lea.common.entities.CountryV2Entity;
import org.marketplace_lea.prometheus.domain.localization.form.CountrySearchCriteria;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CountrySpecifications {

    public static Specification<CountryV2Entity> byCriteria(CountrySearchCriteria criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(criteria.getLabel())) {
                predicates.add(cb.like(cb.lower(root.get("label")), "%" + criteria.getLabel().toLowerCase() + "%"));
            }

            if (StringUtils.hasText(criteria.getCode())) {
                predicates.add(cb.equal(root.get("code"), criteria.getCode()));
            }

            if (StringUtils.hasText(criteria.getCallingCode())) {
                predicates.add(cb.equal(root.get("callingCode"), criteria.getCallingCode()));
            }

            if (criteria.getEnabled() != null) {
                predicates.add(cb.equal(root.get("enabled"), criteria.getEnabled()));
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