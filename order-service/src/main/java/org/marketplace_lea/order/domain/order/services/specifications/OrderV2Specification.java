package org.marketplace_lea.order.domain.order.services.specifications;

import org.marketplace_lea.order.common.entities.order.OrderV2Entity;
import org.marketplace_lea.order.domain.order.form.OrderV2SearchForm;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class OrderV2Specification {

    public static Specification<OrderV2Entity> buildSpecification(OrderV2SearchForm form) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (form != null) {
                // Filtrage par customerId
                if (form.customerId() != null && !form.customerId().isBlank()) {
                    predicates.add(criteriaBuilder.equal(root.get("customer").get("id"), form.customerId()));
                }

                // Filtrage par status
                if (form.status() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("status"), form.status()));
                }

                // Filtrage par delivered
                if (form.delivered() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("delivered"), form.delivered()));
                }

                // Filtrage par hasOnlinePayment
                if (form.hasOnlinePayment() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("hasOnlinePayment"), form.hasOnlinePayment()));
                }

                // Filtrage par provider
                if (form.provider() != null && !form.provider().isBlank()) {
                    predicates.add(criteriaBuilder.equal(root.get("provider"), form.provider()));
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
