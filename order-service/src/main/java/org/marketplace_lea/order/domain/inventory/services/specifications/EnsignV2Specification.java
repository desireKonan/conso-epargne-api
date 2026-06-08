package org.marketplace_lea.order.domain.inventory.services.specifications;

import org.marketplace_lea.order.common.entities.inventory.EnsignV2Entity;
import org.marketplace_lea.order.domain.inventory.dto.EnsignV2SearchForm;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class EnsignV2Specification {

    public static Specification<EnsignV2Entity> buildSpecification(EnsignV2SearchForm form) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtrage par mot-clé (Recherche dans le label ou la description)
            if (form != null && form.searchKeyword() != null && !form.searchKeyword().isBlank()) {
                String pattern = "%" + form.searchKeyword().toLowerCase() + "%";
                Predicate labelPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("label")), pattern);
                Predicate descPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), pattern);
                predicates.add(criteriaBuilder.or(labelPredicate, descPredicate));
            }

            // Filtrage par disponibilité
            if (form != null && form.available() != null) {
                predicates.add(criteriaBuilder.equal(root.get("available"), form.available()));
            }

            // Filtrage par suppression logique (corbeille)
            if (form != null && form.isDeleted() != null) {
                if (form.isDeleted()) {
                    predicates.add(criteriaBuilder.isNotNull(root.get("deletedAt")));
                } else {
                    predicates.add(criteriaBuilder.isNull(root.get("deletedAt")));
                }
            } else {
                // Par défaut, exclure les éléments supprimés
                predicates.add(criteriaBuilder.isNull(root.get("deletedAt")));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
