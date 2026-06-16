package org.marketplace_lea.common.services.account;

import org.marketplace_lea.common.common.dtos.UserPrincipalV2;
import org.marketplace_lea.common.entities.account.AccountV2Entity;
import org.marketplace_lea.common.repositories.account.AccountV2JpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implémentation V2 du service de compte, basée sur {@link AccountV2Entity}
 * (table {@code ce_account_v2}).
 *
 * <p>
 * mais opère sur le nouveau modèle de données V2. Conçu pour être utilisé avec
 * {@link org.marketplace_lea.common.common.service.auth.WebAuthenticationManager}
 * dans le module prometheus-service.
 * </p>
 *
 * <p>
 * Supporte deux modes d'authentification via la propriété
 * {@code app.authentication.country-based} :
 * <ul>
 * <li><b>disabled</b> (défaut) : recherche par login simple</li>
 * <li><b>enabled</b> : recherche par login formatté
 * {@code "login.countryCode"}</li>
 * </ul>
 * </p>
 */
@Slf4j
@Service("accountV2UserDetailsService")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultAccountV2Service implements AccountV2UserDetailsService {
    private final AccountV2JpaRepository accountRepository;

    @Value("${app.authentication.country-based:disabled}")
    private String countryBasedAuthentication;

    // ─────────────────────────────────────────────
    // UserDetailsService
    // ─────────────────────────────────────────────

    /**
     * Charge le compte à partir du login pour Spring Security.
     *
     * <p>
     * En mode <b>country-based</b>, le {@code formattedLogin} est de la forme
     * {@code "login.countryCode"} (produit par
     * {@code AuthDetailsForm.getFormattedLogin()}).<br>
     * En mode <b>simple</b>, le {@code formattedLogin} est le login brut.
     * </p>
     *
     * @param formattedLogin login (simple ou formatté selon le mode d'auth)
     * @return {@link UserPrincipalV2} encapsulant le compte V2 trouvé
     * @throws UsernameNotFoundException si aucun compte actif ne correspond
     */
    @Override
    public UserDetails loadUserByUsername(String formattedLogin) throws UsernameNotFoundException {
        log.debug("[DefaultAccountV2Service.loadUserByUsername] Chargement du compte: {}", formattedLogin);
        try {
            AccountV2Entity account = "enabled".equalsIgnoreCase(countryBasedAuthentication)
                    ? findByFormattedLogin(formattedLogin)
                            .orElseThrow(
                                    () -> new UsernameNotFoundException("Compte introuvable pour : " + formattedLogin))
                    : findByLogin(formattedLogin)
                            .orElseThrow(
                                    () -> new UsernameNotFoundException("Compte introuvable pour : " + formattedLogin));

            log.debug("[DefaultAccountV2Service.loadUserByUsername] Compte trouvé: {}", account.getLogin());
            return new UserPrincipalV2(account);
        } catch (UsernameNotFoundException e) {
            log.error("[DefaultAccountV2Service.loadUserByUsername] Login introuvable: {}", formattedLogin);
            throw e;
        }
    }

    // ─────────────────────────────────────────────
    // Recherche
    // ─────────────────────────────────────────────

    /**
     * {@inheritDoc}
     *
     * <p>
     * Utilise {@code getByActiveLogin} : ne retourne que les comptes non supprimés
     * ({@code deletedAt IS NULL}).
     * </p>
     */
    @Override
    public Optional<AccountV2Entity> findByLogin(String login) {
        log.debug("[DefaultAccountV2Service.findByLogin] Recherche compte actif pour login: {}", login);
        return accountRepository.getByActiveLogin(login);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Parse le format {@code "login.countryCode"} en deux tokens
     * séparés par un point, puis recherche le compte correspondant.
     * </p>
     *
     * @throws IllegalArgumentException si le format est invalide (ne contient pas
     *                                  de point)
     */
    @Override
    public Optional<AccountV2Entity> findByFormattedLogin(String loginWithCountryCode) {
        log.debug("[DefaultAccountV2Service.findByFormattedLogin] Recherche compte pour: {}", loginWithCountryCode);
        String[] parts = loginWithCountryCode.split("\\.");
        if (parts.length < 2) {
            log.error("[DefaultAccountV2Service.findByFormattedLogin] Format invalide (attendu: login.countryCode): {}",
                    loginWithCountryCode);
            throw new IllegalArgumentException("Format de login invalide. Attendu : 'login.countryCode'");
        }
        String login = parts[0];
        String countryCode = parts[1];
        return accountRepository.getByActiveLogin(login)
                .filter(acc -> countryCode.equalsIgnoreCase(acc.getCountryCode()));
    }

    // ─────────────────────────────────────────────
    // Vérifications d'existence
    // ─────────────────────────────────────────────

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsByLogin(String login) {
        return accountRepository.getByActiveLogin(login).isPresent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsByLoginAndCountryCode(String login, String countryCode) {
        return accountRepository.existsByLoginAndCode(login, countryCode);
    }
}
