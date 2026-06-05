package org.marketplace_lea.prometheus.domain.customer.services;

import org.marketplace_lea.common.dtos.ResponseDTO;
import org.marketplace_lea.common.forms.UpdateCredentialForm;
import org.marketplace_lea.common.forms.UpdateCustomerForm;
import org.marketplace_lea.prometheus.domain.customer.dto.CustomerChildrenDTO;
import org.marketplace_lea.prometheus.domain.customer.dto.CustomerCpmDTO;
import org.marketplace_lea.prometheus.domain.customer.dto.CustomerProfileDTO;
import org.marketplace_lea.prometheus.domain.customer.dto.CustomerStatisticsDTO;

import java.util.Map;

/**
 * Contrat de service pour la gestion du profil client (V2).
 *
 * <p>
 * Définit les opérations disponibles pour le client connecté :
 * récupération du profil, statistiques, CPM, mise à jour des informations
 * personnelles et des identifiants, et suppression du compte.
 * </p>
 *
 * <p>
 * Ce service remplace les fonctionnalités de
 * {@link com.conso_epargne.application.facades.api.CustomerFacade}
 * et {@link org.marketplace_lea.common.services.ConsoStatisticCalculator}
 * dans le module {@code application}, en s'appuyant sur les entités V2
 * ({@link org.marketplace_lea.common.entities.v2.customer.CustomerV2Entity}).
 * </p>
 *
 * @see org.marketplace_lea.prometheus.domain.customer.services.impl.DefaultCustomerProfileService
 */
public interface CustomerProfileService {

    // ─────────────────────────────────────────────
    // Consultation du profil
    // ─────────────────────────────────────────────

    /**
     * Récupère les informations du profil du client connecté.
     *
     * @return le profil complet du client
     */
    CustomerProfileDTO getCurrentCustomerProfile();

    // ─────────────────────────────────────────────
    // Réseau de parrainage (enfants / filleuls)
    // ─────────────────────────────────────────────

    /**
     * Récupère la liste des filleuls du client connecté, organisés par niveau.
     *
     * @return les filleuls regroupés par niveau avec le total
     */
    CustomerChildrenDTO getChildren();

    /**
     * Récupère les statistiques du réseau du client connecté,
     * organisées par niveau de parrainage.
     *
     * @return les statistiques par niveau (points générés, commissions, compteurs)
     */
    Map<String, CustomerStatisticsDTO> getStatistics();

    // ─────────────────────────────────────────────
    // CPM (Consommation Par Mois)
    // ─────────────────────────────────────────────

    /**
     * Calcule et retourne le CPM du client connecté.
     *
     * @return le CPM
     */
    CustomerCpmDTO getCpm();

    // ─────────────────────────────────────────────
    // Mise à jour du profil
    // ─────────────────────────────────────────────

    /**
     * Met à jour les informations personnelles du client connecté.
     *
     * @param form formulaire de mise à jour (nom, prénom, email)
     * @return un message de confirmation
     */
    ResponseDTO updateCustomerInfo(UpdateCustomerForm form);

    /**
     * Met à jour le mot de passe du client connecté.
     *
     * @param form formulaire contenant l'ancien et le nouveau mot de passe
     * @return un message de confirmation
     */
    ResponseDTO updateCredential(UpdateCredentialForm form);

    // ─────────────────────────────────────────────
    // Suppression du compte
    // ─────────────────────────────────────────────

    /**
     * Désactive (soft-delete) le compte du client connecté.
     *
     * @return un message de confirmation
     */
    ResponseDTO dismissAccount();
}
