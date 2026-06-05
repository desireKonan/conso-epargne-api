package org.marketplace_lea.prometheus.domain.customer.controllers;

import org.marketplace_lea.common.common.constants.Router;
import org.marketplace_lea.common.dtos.ResponseDTO;
import org.marketplace_lea.common.forms.UpdateCredentialForm;
import org.marketplace_lea.common.forms.UpdateCustomerForm;
import org.marketplace_lea.prometheus.domain.customer.dto.CustomerChildrenDTO;
import org.marketplace_lea.prometheus.domain.customer.dto.CustomerCpmDTO;
import org.marketplace_lea.prometheus.domain.customer.dto.CustomerProfileDTO;
import org.marketplace_lea.prometheus.domain.customer.dto.CustomerStatisticsDTO;
import org.marketplace_lea.prometheus.domain.customer.services.CustomerProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Contrôleur REST V2 pour la gestion du profil client.
 *
 * <p>
 * Remplace les endpoints du {@code CustomerApiController} (module {@code application})
 * en s'appuyant sur les entités V2 et le module {@code prometheus-service}.
 * </p>
 *
 * <p>Endpoints exposés :</p>
 * <ul>
 *   <li>{@code GET    /api/v2/me}              — Profil du client connecté</li>
 *   <li>{@code GET    /api/v2/me/children}     — Filleuls par niveau</li>
 *   <li>{@code GET    /api/v2/me/children/statistics} — Statistiques réseau</li>
 *   <li>{@code GET    /api/v2/me/cpm}          — CPM du client</li>
 *   <li>{@code POST   /api/v2/me}              — Mise à jour du profil</li>
 *   <li>{@code POST   /api/v2/me/credentials}  — Changement de mot de passe</li>
 *   <li>{@code DELETE /api/v2/me/dismiss}       — Suppression du compte</li>
 * </ul>
 *
 * @see CustomerProfileService
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(Router.ME)
public class CustomerProfileV2Controller {
    private final CustomerProfileService customerProfileService;

    // ─────────────────────────────────────────────
    // Consultation
    // ─────────────────────────────────────────────

    /**
     * Récupère les informations du profil du client connecté.
     *
     * @return le profil complet (informations personnelles + compte)
     */
    @GetMapping()
    public ResponseEntity<CustomerProfileDTO> getCustomerProfile() {
        log.info("[CustomerProfileV2Controller.getCustomerProfile] Récupération du profil");
        return ResponseEntity.ok(customerProfileService.getCurrentCustomerProfile());
    }

    /**
     * Récupère les filleuls du client connecté, organisés par niveau.
     *
     * @return les filleuls avec le nombre total et le regroupement par niveau
     */
    @GetMapping(value = Router.CHILDREN)
    public ResponseEntity<CustomerChildrenDTO> getCustomerChildren() {
        log.info("[CustomerProfileV2Controller.getCustomerChildren] Récupération des filleuls");
        return ResponseEntity.ok(customerProfileService.getChildren());
    }

    /**
     * Récupère les statistiques du réseau du client connecté.
     *
     * @return statistiques par niveau (points, commissions, compteurs)
     */
    @GetMapping(value = Router.CHILDREN + "/statistics")
    public ResponseEntity<Map<String, CustomerStatisticsDTO>> getStatistics() {
        log.info("[CustomerProfileV2Controller.getStatistics] Récupération des statistiques réseau");
        return ResponseEntity.ok(customerProfileService.getStatistics());
    }

    /**
     * Récupère le CPM (Consommation Par Mois) du client connecté.
     *
     * @return le CPM
     */
    @GetMapping("/cpm")
    public ResponseEntity<CustomerCpmDTO> getCpm() {
        log.info("[CustomerProfileV2Controller.getCpm] Calcul du CPM");
        return ResponseEntity.ok(customerProfileService.getCpm());
    }

    // ─────────────────────────────────────────────
    // Mise à jour
    // ─────────────────────────────────────────────

    /**
     * Met à jour les informations personnelles du client connecté.
     *
     * @param form formulaire contenant les nouvelles informations (nom, prénom, email)
     * @return message de confirmation
     */
    @PostMapping
    public ResponseEntity<ResponseDTO> updateCustomerInfo(@Valid @RequestBody UpdateCustomerForm form) {
        log.info("[CustomerProfileV2Controller.updateCustomerInfo] Mise à jour du profil");
        return ResponseEntity.ok(customerProfileService.updateCustomerInfo(form));
    }

    /**
     * Met à jour le mot de passe du client connecté.
     *
     * @param form formulaire contenant l'ancien et le nouveau mot de passe
     * @return message de confirmation
     */
    @PostMapping("/credentials")
    public ResponseEntity<ResponseDTO> updateCredential(@Valid @RequestBody UpdateCredentialForm form) {
        log.info("[CustomerProfileV2Controller.updateCredential] Changement de mot de passe");
        return ResponseEntity.ok(customerProfileService.updateCredential(form));
    }

    // ─────────────────────────────────────────────
    // Suppression
    // ─────────────────────────────────────────────

    /**
     * Désactive (soft-delete) le compte du client connecté.
     *
     * @return message de confirmation
     */
    @DeleteMapping("/dismiss")
    public ResponseEntity<ResponseDTO> dismiss() {
        log.info("[CustomerProfileV2Controller.dismiss] Suppression du compte");
        return ResponseEntity.ok(customerProfileService.dismissAccount());
    }
}
