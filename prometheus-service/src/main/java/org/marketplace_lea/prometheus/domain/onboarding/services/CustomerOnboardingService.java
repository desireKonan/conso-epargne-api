package org.marketplace_lea.prometheus.domain.onboarding.services;

import org.marketplace_lea.common.dtos.CustomerV2DTO;
import org.marketplace_lea.common.forms.RegistrationV2Form;

/**
 * Service responsable de l'inscription (onboarding) d'un nouveau client.
 * 
 * <p>Ce service gère uniquement la création du client, de son compte,
 * de ses adresses, numéros de téléphone et l'initialisation des portefeuilles.
 * Les notifications (email, SMS) sont déléguées à un service distinct.
 * </p>
 * 
 * <p>Cette interface suit le principe de ségrégation des interfaces (ISP)
 * et d'inversion des dépendances (DIP).</p>
 */
public interface CustomerOnboardingService {
    /**
     * Inscrit un nouveau client à partir du formulaire d'inscription.
     * 
     * <p>Effectue les opérations suivantes :</p>
     * <ul>
     *   <li>Validation des données (pays, longueur téléphone, quartier, code parrain)</li>
     *   <li>Création de l'entité {@code Customer} et de son {@code Account}</li>
     *   <li>Création des adresses et numéros de téléphone associés</li>
     *   <li>Persistance en base de données</li>
     *   <li>Initialisation des portefeuilles personnel et LEA</li>
     * </ul>
     * 
     * <p>Les notifications (email/SMS) ne sont pas envoyées par ce service,
     * mais doivent être déclenchées par l'orchestrateur après un succès.</p>
     * 
     * @param form Formulaire d'inscription contenant les informations du client
     * @return Le compte ({@code Account}) créé et persisté
     */
    CustomerV2DTO onboard(RegistrationV2Form form);
}