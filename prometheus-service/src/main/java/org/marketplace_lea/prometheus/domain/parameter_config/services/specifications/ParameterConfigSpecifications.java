package org.marketplace_lea.prometheus.domain.parameter_config.services.specifications;

import jakarta.persistence.criteria.Predicate;
import org.marketplace_lea.common.entities.parameters.ParameterConfigEntity;
import org.marketplace_lea.prometheus.domain.parameter_config.form.ParameterConfigSearchCriteria;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ParameterConfigSpecifications {

    public static Specification<ParameterConfigEntity> byCriteria(ParameterConfigSearchCriteria criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(criteria.getKey())) {
                predicates.add(cb.like(cb.lower(root.get("key")),
                        "%" + criteria.getKey().toLowerCase() + "%"));
            }

            if (StringUtils.hasText(criteria.getValue())) {
                predicates.add(cb.like(cb.lower(root.get("value")),
                        "%" + criteria.getValue().toLowerCase() + "%"));
            }

            if (StringUtils.hasText(criteria.getDataType())) {
                predicates.add(cb.equal(root.get("dataType"), criteria.getDataType()));
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