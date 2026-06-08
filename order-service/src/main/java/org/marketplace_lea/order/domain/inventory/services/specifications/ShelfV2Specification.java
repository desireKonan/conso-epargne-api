package org.marketplace_lea.order.domain.inventory.services.specifications;

import jakarta.persistence.criteria.Predicate;
import org.marketplace_lea.order.common.entities.inventory.ShelfV2Entity;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ShelfV2Specification {

    public static Specification<ShelfV2Entity> buildSpecification(String ensignId) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtrage par Enseigne
            if (ensignId != null && !ensignId.isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("ensign").get("id"), ensignId));
            }

            // Filtrage par suppression logique (corbeille)
            predicates.add(criteriaBuilder.isNull(root.get("deletedAt")));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
