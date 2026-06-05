package org.marketplace_lea.prometheus.domain.customer.services.impl;

import org.marketplace_lea.common.common.exceptions.ConsoEpargneException;
import org.marketplace_lea.common.common.exceptions.ConsoEpargneNotFoundDataException;
import org.marketplace_lea.common.dtos.CustomerV2DTO;
import org.marketplace_lea.common.entities.customer.CustomerV2Entity;
import org.marketplace_lea.common.forms.CreateCustomerForm;
import org.marketplace_lea.prometheus.domain.onboarding.forms.CustomerV2Mapper;
import org.marketplace_lea.common.repositories.customer.CustomerV2JpaRepository;
import org.marketplace_lea.prometheus.domain.customer.forms.CustomerLockForm;
import org.marketplace_lea.prometheus.domain.customer.forms.CustomerSearchForm;
import org.marketplace_lea.prometheus.domain.customer.services.CustomerApiService;
import org.marketplace_lea.prometheus.domain.customer.specifications.CustomerSpecifications;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Implémentation du service de gestion des clients (V2).
 *
 * <p>
 * Cette classe fournit les opérations CRUD et la logique métier
 * associée à l'entité {@link CustomerV2Entity}. Elle s'appuie sur
 * le repository {@link CustomerV2JpaRepository} et suit le pattern
 * interface / implémentation du projet.
 * </p>
 *
 * @see CustomerV2Entity
 * @see CustomerV2JpaRepository
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultCustomerService implements CustomerApiService {
    private final CustomerV2JpaRepository customerRepository;
    private final CustomerV2Mapper customerV2Mapper;

    private final EntityManager entityManager;

    // ─────────────────────────────────────────────
    // Recherche
    // ─────────────────────────────────────────────

    /**
     * Recherche un client par son identifiant.
     *
     * @param id identifiant du client
     * @return le client trouvé
     * @throws ConsoEpargneNotFoundDataException si aucun client ne correspond
     */
    @Override
    public CustomerV2DTO findById(String id) {
        log.debug("[CustomerServiceImpl.findById] Recherche du client ID: {}", id);
        return customerRepository.findById(id)
                .filter(customerV2Entity -> Objects.isNull(customerV2Entity.getDeletedAt()) || customerV2Entity.accountBlocked())
                .map(this.customerV2Mapper::toDTO)
                .orElseThrow(() -> new ConsoEpargneNotFoundDataException("Le client avec l'ID '" + id + "' n'existe pas."));
    }

    /**
     * Recherche un client par le login de son compte.
     *
     * @param login login du compte associé
     * @return un {@link Optional} contenant le client, vide si non trouvé
     */
    @Override
    public Optional<CustomerV2DTO> findByLogin(String login) {
        log.debug("[CustomerServiceImpl.findByLogin] Recherche du client avec login: {}", login);
        return customerRepository.getByLogin(login)
                .map(this.customerV2Mapper::toDTO);
    }

    /**
     * Recherche un client par login et lève une exception s'il n'existe pas.
     *
     * @param login login du compte associé
     * @return le client trouvé
     * @throws ConsoEpargneException si aucun client ne correspond
     */
    @Override
    public CustomerV2DTO getByLoginOrThrow(String login) {
        return findByLogin(login)
                .orElseThrow(() -> new ConsoEpargneException("Aucun client trouvé pour le login '" + login + "'.", HttpStatus.NOT_FOUND));
    }

    // ─────────────────────────────────────────────
    // Persistance
    // ─────────────────────────────────────────────

    /**
     * Persiste un nouveau client en base de données.
     *
     * @param form l'entité client à sauvegarder
     * @return le client sauvegardé avec son identifiant généré
     */
    @Override
    public CustomerV2DTO save(CreateCustomerForm form) {
        log.info("[CustomerServiceImpl.save] Enregistrement d'un nouveau client: {}", form.id());
        var entity = customerV2Mapper.toEntity(form);
        CustomerV2Entity saved = customerRepository.save(entity);
        log.info("[CustomerServiceImpl.save] Client enregistré avec l'ID: {}", saved.getId());
        return this.customerV2Mapper.toDTO(saved);
    }

    /**
     * Met à jour un client existant.
     *
     * <p>
     * Vérifie l'existence du client avant la mise à jour et
     * positionne automatiquement la date de modification.
     * </p>
     *
     * @param form l'entité client avec les modifications
     * @return le client mis à jour
     * @throws ConsoEpargneNotFoundDataException si le client n'existe pas
     */
    @Transactional
    @Override
    public CustomerV2DTO update(CreateCustomerForm form) {
        log.info("[CustomerServiceImpl.update] Mise à jour du client ID: {}", form.id());

        if (!customerRepository.existsById(form.id())) {
            throw new ConsoEpargneNotFoundDataException(
                    "Impossible de mettre à jour : le client avec l'ID '" + form.id() + "' n'existe pas.");
        }

        var entity = customerV2Mapper.toEntity(form);
        entity.setUpdatedAt(LocalDateTime.now());
        CustomerV2Entity updated = customerRepository.save(entity);
        log.info("[CustomerServiceImpl.update] Client ID: {} mis à jour avec succès", updated.getId());
        return this.customerV2Mapper.toDTO(updated);
    }

    /**
     * Supprime un client par son identifiant.
     *
     * @param id identifiant du client à supprimer
     * @throws ConsoEpargneNotFoundDataException si le client n'existe pas
     */
    @Transactional
    @Override
    public void deleteById(String id) {
        log.info("[CustomerServiceImpl.deleteById] Suppression du client ID: {}", id);

        var customerFound = customerRepository.findById(id);

        customerFound.ifPresent(customer -> {
            customer.setDeletedAt(LocalDateTime.now());
            customerRepository.save(customer);
            log.info("[CustomerServiceImpl.deleteById] Client ID: {} supprimé", id);
        });
    }

    // ─────────────────────────────────────────────
    // Gestion de l'état du compte
    // ─────────────────────────────────────────────


    /**
     * Déverrouille ou vérouille le compte d'un client.
     *
     * @param customerLockForm formulaire pour vérouiller ou déverouiller un compte client.
     */
    @Override
    @Transactional
    public void unlockAccount(CustomerLockForm customerLockForm) {
        log.info("[CustomerServiceImpl.unlockAccount] Déverrouillage du compte pour le client ID: {}", customerLockForm.customerId());
        Optional<CustomerV2Entity> customerFound = this.customerRepository.findById(customerLockForm.customerId());

        customerFound.ifPresent(customer -> {
            customer.getAccount().setBlacklisted(customerLockForm.unlocked());
            customer.setUpdatedAt(LocalDateTime.now());
            customerRepository.save(customer);
            log.info("[CustomerServiceImpl.unlockAccount] Compte du client ID: {} déverrouillé", customerLockForm.customerId());
        });
    }

    // ─────────────────────────────────────────────
    // Vérifications d'existence
    // ─────────────────────────────────────────────

    /**
     * Vérifie si un client existe pour un login donné.
     *
     * @param login login à vérifier
     * @return {@code true} si un client existe avec ce login
     */
    @Override
    public boolean existsByLogin(String login) {
        return customerRepository.existsForLogin(login);
    }

    /**
     * Vérifie si un client existe avec un code de parrainage donné.
     *
     * @param code code de parrainage à vérifier
     * @return {@code true} si un client existe avec ce code
     */
    @Override
    public boolean existsByAffiliationCode(String code) {
        return customerRepository.existsForAffiliationCode(code);
    }

    /**
     * Vérifie l'unicité du login et lève une exception s'il est déjà utilisé.
     *
     * @param login login à valider
     * @throws ConsoEpargneException si le login est déjà pris
     */
    @Override
    public void ensureLoginIsAvailable(String login) {
        if (existsByLogin(login)) {
            throw new ConsoEpargneException("Le login existe déjà !", HttpStatus.CONFLICT);
        }
    }

    // ─────────────────────────────────────────────
    // Statistiques
    // ─────────────────────────────────────────────

    /**
     * Retourne le nombre total de clients.
     *
     * @return le nombre de clients
     */
    @Override
    public long count() {
        return customerRepository.count();
    }

    /**
     * Retourne le nombre de clients pour un type de compte donné.
     *
     * @param accountTypeId identifiant du type de compte
     * @return le nombre de clients correspondants
     */
    @Override
    public long countByAccountType(String accountTypeId) {
        return customerRepository.countByAccountTypeId(accountTypeId);
    }

    /**
     * Retourne la liste des clients de manière paginée.
     *
     * @param searchForm identifiant du type de compte
     * @param pageable   identifiant du type de compte
     * @return le nombre de clients correspondants
     */
    @Override
    public Page<CustomerV2DTO> fetchCustomer(CustomerSearchForm searchForm, Pageable pageable) {
        // 1. Construction de la requête principale avec JOIN FETCH
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<CustomerV2Entity> query = cb.createQuery(CustomerV2Entity.class);
        Root<CustomerV2Entity> root = query.from(CustomerV2Entity.class);

        // JOIN FETCH pour éviter N+1
        root.join("account", JoinType.INNER);

        // 2. Appliquer les filtres dynamiques
        List<Predicate> predicates = CustomerSpecifications.buildPredicates(searchForm, cb, root);
        if (!predicates.isEmpty()) {
            query.where(predicates.toArray(new Predicate[0]));
        }

        // 3. Gestion du tri (sort)
        if (pageable.getSort().isSorted()) {
            List<Order> orders = new ArrayList<>();
            pageable.getSort().forEach(order -> {
                Path<Object> path = root.get(order.getProperty());
                orders.add(order.isAscending() ? cb.asc(path) : cb.desc(path));
            });
            query.orderBy(orders);
        }

        // 4. Exécution de la requête avec pagination
        TypedQuery<CustomerV2Entity> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<CustomerV2Entity> entities = typedQuery.getResultList();

        // 5. Requête de comptage (pour le nombre total d'éléments)
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<CustomerV2Entity> countRoot = countQuery.from(CustomerV2Entity.class);
        countQuery.select(cb.count(countRoot));

        Long total = entityManager.createQuery(countQuery)
                .getSingleResult();

        // 6. Conversion en DTO et retour
        List<CustomerV2DTO> dtos = entities.stream()
                .map(this.customerV2Mapper::toDTO)
                .toList();

        return new PageImpl<>(dtos, pageable, total);
    }
}
