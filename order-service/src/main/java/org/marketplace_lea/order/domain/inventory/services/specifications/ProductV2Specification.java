package org.marketplace_lea.order.domain.inventory.services.specifications;

import org.marketplace_lea.order.common.entities.inventory.ProductV2Entity;
import org.marketplace_lea.order.domain.inventory.dto.ProductV2Criteria;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductV2Specification {

    public static Specification<ProductV2Entity> buildSpecification(ProductV2Criteria criteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtrage par mot-clé (Recherche dans le label ou la description)
            if (criteria.getSearchKeyword() != null && !criteria.getSearchKeyword().isBlank()) {
                String pattern = "%" + criteria.getSearchKeyword().toLowerCase() + "%";
                Predicate labelPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("label")), pattern);
                Predicate descPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), pattern);
                predicates.add(criteriaBuilder.or(labelPredicate, descPredicate));
            }

            // Filtrage par Enseigne
            if (criteria.getEnsignId() != null && !criteria.getEnsignId().isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("shelf").get("ensign").get("id"), criteria.getEnsignId()));
            }

            // Filtrage par statut de stock (soldOut)
            if (criteria.getSoldOut() != null) {
                predicates.add(criteriaBuilder.equal(root.get("soldOut"), criteria.getSoldOut()));
            }

            // Filtrage par suppression logique (corbeille)
            if (criteria.getIsDeleted() != null) {
                if (criteria.getIsDeleted()) {
                    predicates.add(criteriaBuilder.isNotNull(root.get("deletedAt")));
                } else {
                    predicates.add(criteriaBuilder.isNull(root.get("deletedAt")));
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
