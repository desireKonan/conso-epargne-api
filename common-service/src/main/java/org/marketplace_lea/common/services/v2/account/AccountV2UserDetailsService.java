package org.marketplace_lea.common.services.v2.account;

import org.marketplace_lea.common.entities.account.AccountV2Entity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

/**
 * Contrat de service pour la gestion des comptes V2 ({@link AccountV2Entity}).
 *
 * <p>Étend {@link UserDetailsService} pour intégration avec Spring Security
 * (utilisé notamment par {@link org.marketplace_lea.common.common.service.auth.WebAuthenticationManager}).</p>
 *
 * <p>Supporte deux modes d'authentification :</p>
 * <ul>
 *   <li><b>Simple</b> : recherche par {@code login} uniquement</li>
 *   <li><b>Country-based</b> : recherche par {@code login.countryCode} (format formatté)</li>
 * </ul>
 */
public interface AccountV2UserDetailsService extends UserDetailsService {

    // ─────────────────────────────────────────────
    // Recherche
    // ─────────────────────────────────────────────

    /**
     * Recherche un compte actif (non supprimé) par son login.
     *
     * @param login login du compte
     * @return le compte trouvé
     */
    Optional<AccountV2Entity> findByLogin(String login);

    /**
     * Recherche un compte actif par login et code pays.
     * Le paramètre {@code loginWithCountryCode} est au format {@code "login.countryCode"}.
     *
     * @param loginWithCountryCode login formatté ({@code "login.countryCode"})
     * @return le compte trouvé
     */
    Optional<AccountV2Entity> findByFormattedLogin(String loginWithCountryCode);

    // ─────────────────────────────────────────────
    // Vérifications d'existence
    // ─────────────────────────────────────────────

    /**
     * Vérifie si un compte actif existe pour un login donné.
     *
     * @param login login à vérifier
     * @return {@code true} si un compte actif existe avec ce login
     */
    boolean existsByLogin(String login);

    /**
     * Vérifie si un compte existe pour un login et un code pays donnés.
     *
     * @param login       login à vérifier
     * @param countryCode code pays ISO alpha-3
     * @return {@code true} si un compte existe avec ce login et ce code pays
     */
    boolean existsByLoginAndCountryCode(String login, String countryCode);
}
