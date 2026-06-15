package org.marketplace_lea.common.services.transaction.specifications;


import jakarta.persistence.criteria.Predicate;
import org.marketplace_lea.common.entities.transaction.TransactionStatus;
import org.marketplace_lea.common.entities.transaction.TransactionType;
import org.marketplace_lea.common.entities.transaction.TransactionV2Entity;
import org.marketplace_lea.common.forms.transactions.TransactionV2SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class TransactionSpecification {
    public static Specification<TransactionV2Entity> buildSpecification(TransactionV2SearchCriteria criteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getId() != null && !criteria.getId().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("id"), criteria.getId()));
            }

            if (criteria.getSourceWalletId() != null && !criteria.getSourceWalletId().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("source").get("id"), criteria.getSourceWalletId()));
            }

            if (criteria.getDestinationWalletId() != null && !criteria.getDestinationWalletId().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("destination").get("id"), criteria.getDestinationWalletId()));
            }

            if (criteria.getPhoneNumber() != null && !criteria.getPhoneNumber().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("phoneNumber"), "%" + criteria.getPhoneNumber() + "%"));
            }

            if (criteria.getPaymentReference() != null && !criteria.getPaymentReference().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("paymentReference"), "%" + criteria.getPaymentReference() + "%"));
            }

            if (criteria.getObjectId() != null && !criteria.getObjectId().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("objectId"), criteria.getObjectId()));
            }

            if (criteria.getTransactionStatus() != null && !criteria.getTransactionStatus().isEmpty()) {
                TransactionStatus status = TransactionStatus.valueOf(criteria.getTransactionStatus());
                predicates.add(criteriaBuilder.equal(root.get("transactionStatus"), status));
            }

            if (criteria.getTransactionType() != null && !criteria.getTransactionType().isEmpty()) {
                TransactionType type = TransactionType.fromLabel(criteria.getTransactionType());
                predicates.add(criteriaBuilder.equal(root.get("transactionType"), type));
            }

            if (criteria.getCurrency() != null && !criteria.getCurrency().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("currency"), criteria.getCurrency()));
            }

            if (criteria.getStartDate() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), criteria.getStartDate()));
            }

            if (criteria.getEndDate() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), criteria.getEndDate()));
            }

            if (criteria.getMinAmount() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("amount"), criteria.getMinAmount()));
            }

            if (criteria.getMaxAmount() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("amount"), criteria.getMaxAmount()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
