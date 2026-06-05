package org.marketplace_lea.prometheus.domain.customer.services;

import org.marketplace_lea.common.dtos.CustomerV2DTO;
import org.marketplace_lea.common.forms.CreateCustomerForm;
import org.marketplace_lea.prometheus.domain.customer.forms.CustomerLockForm;
import org.marketplace_lea.prometheus.domain.customer.forms.CustomerSearchForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Contrat de service pour la gestion des clients (V2).
 *
 * <p>
 * Définit l'ensemble des opérations disponibles sur l'entité
 * {@link CustomerV2Entity} : recherche, persistance, gestion
 * de l'état du compte, vérifications d'existence et statistiques.
 * </p>
 *
 * @see CustomerV2Entity
 * @see org.marketplace_lea.prometheus.domain.customer.services.impl.DefaultCustomerService
 */
public interface CustomerApiService {

    // ─────────────────────────────────────────────
    // Recherche
    // ─────────────────────────────────────────────

    /**
     * Recherche un client par son identifiant.
     *
     * @param id identifiant du client
     * @return le client trouvé
     */
    CustomerV2DTO findById(String id);

    /**
     * Recherche un client par le login de son compte.
     *
     * @param login login du compte associé
     * @return un {@link Optional} contenant le client, vide si non trouvé
     */
    Optional<CustomerV2DTO> findByLogin(String login);

    /**
     * Recherche un client par login et lève une exception s'il n'existe pas.
     *
     * @param login login du compte associé
     * @return le client trouvé
     */
    CustomerV2DTO getByLoginOrThrow(String login);

    // ─────────────────────────────────────────────
    // Persistance
    // ─────────────────────────────────────────────

    /**
     * Persiste un nouveau client en base de données.
     *
     * @param form l'entité client à sauvegarder
     * @return le client sauvegardé avec son identifiant généré
     */
    CustomerV2DTO save(CreateCustomerForm form);

    /**
     * Met à jour un client existant.
     *
     * @param form l'entité client avec les modifications
     * @return le client mis à jour
     */
    CustomerV2DTO update(CreateCustomerForm form);

    /**
     * Supprime un client par son identifiant.
     *
     * @param id identifiant du client à supprimer
     */
    void deleteById(String id);

    // ─────────────────────────────────────────────
    // Gestion de l'état du compte
    // ─────────────────────────────────────────────


    /**
     * Déverrouille ou déverrouille un compte d'un client.
     *
     * @param customerLockForm formulaire pour vérouiller ou dévérrouiller un compte client.
     */
    void unlockAccount(CustomerLockForm customerLockForm);

    // ─────────────────────────────────────────────
    // Vérifications d'existence
    // ─────────────────────────────────────────────

    /**
     * Vérifie si un client existe pour un login donné.
     *
     * @param login login à vérifier
     * @return {@code true} si un client existe avec ce login
     */
    boolean existsByLogin(String login);

    /**
     * Vérifie si un client existe avec un code de parrainage donné.
     *
     * @param code code de parrainage à vérifier
     * @return {@code true} si un client existe avec ce code
     */
    boolean existsByAffiliationCode(String code);

    /**
     * Vérifie l'unicité du login et lève une exception s'il est déjà utilisé.
     *
     * @param login login à valider
     */
    void ensureLoginIsAvailable(String login);

    // ─────────────────────────────────────────────
    // Statistiques
    // ─────────────────────────────────────────────

    /**
     * Retourne le nombre total de clients.
     *
     * @return le nombre de clients
     */
    long count();

    /**
     * Retourne le nombre de clients pour un type de compte donné.
     *
     * @param accountTypeId identifiant du type de compte
     * @return le nombre de clients correspondants
     */
    long countByAccountType(String accountTypeId);


    Page<CustomerV2DTO> fetchCustomer(CustomerSearchForm searchForm, Pageable pageable);
}
