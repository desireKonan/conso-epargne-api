package org.marketplace_lea.prometheus.domain.customer.specifications;

import org.marketplace_lea.common.entities.customer.CustomerV2Entity;
import org.marketplace_lea.prometheus.domain.customer.forms.CustomerSearchForm;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public final class CustomerSpecifications {

    /**
     * Construit la liste des prédicats (filtres) à partir du formulaire de recherche.
     */
    public static List<Predicate> buildPredicates(
            CustomerSearchForm form,
            CriteriaBuilder cb,
            Root<CustomerV2Entity> root
    ) {
        List<Predicate> predicates = new ArrayList<>();

        if (form == null) {
            return predicates;
        }

        // email : exact match case-insensitive
        if (StringUtils.hasText(form.email())) {
            predicates.add(cb.equal(
                    cb.lower(root.get("email")),
                    form.email()
            ));
        }

        // account.login : exact match case-insensitive (accès via jointure implicite)
        if (StringUtils.hasText(form.login())) {
            Join<Object, Object> accountJoin = root.join("account", JoinType.INNER);
            predicates.add(cb.equal(
                    cb.lower(accountJoin.get("login")),
                    form.login().toLowerCase()
            ));
        }

        if (StringUtils.hasText(form.affiliationCode())) {
            Join<Object, Object> accountJoin = root.join("account", JoinType.INNER);
            predicates.add(cb.like(
                    cb.lower(accountJoin.get("affiliationCode")),
                    form.affiliationCode()
            ));
        }

        if (StringUtils.hasText(form.affiliationCode())) {
            Join<Object, Object> accountJoin = root.join("account", JoinType.INNER);
            predicates.add(cb.like(
                    cb.lower(accountJoin.get("affiliationCode")),
                    form.affiliationCode()
            ));
        }

        if (StringUtils.hasText(form.accountTypeId())) {
            predicates.add(cb.like(
                    cb.lower(root.get("account").get("accountType").get("id")),
                    form.accountTypeId()
            ));
        }

        // Exclusion des soft-deleted (deletedAt IS NULL)
        predicates.add(cb.isNull(root.get("deletedAt")));

        return predicates;
    }
}
