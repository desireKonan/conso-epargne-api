package org.marketplace_lea.prometheus.config;

import org.marketplace_lea.common.common.service.auth.WebAuthenticationManager;
import org.marketplace_lea.common.services.v2.account.AccountV2UserDetailsService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration Spring Security pour le module prometheus-service.
 *
 * <p>
 * Déclare le bean {@link WebAuthenticationManager} utilisé par
 * {@link org.marketplace_lea.prometheus.domain.auth.services.impl.DefaultAuthenticationV2Service}
 * pour authentifier les clients via la méthode {@code login}.
 * </p>
 *
 * <p>
 * Le
 * {@link org.marketplace_lea.common.services.v2.account.DefaultAccountV2Service}
 * ({@code "accountV2UserDetailsService"}) est injecté comme
 * {@code UserDetailsService},
 * opérant sur le modèle {@code AccountV2Entity} (table {@code ce_account_v2})
 * et retournant un
 * {@link org.marketplace_lea.common.common.dtos.UserPrincipalV2}.
 * </p>
 */
@Configuration
public class PrometheusSecurityConfig {

    /**
     * Crée un {@link WebAuthenticationManager} configuré avec le service de détails
     * utilisateur de l'API ({@code AccountService}) et l'encodeur de mot de passe.
     *
     * @param userDetailsService le service de chargement du compte
     *                           ({@code AccountService})
     * @param passwordEncoder    l'encodeur BCrypt partagé
     * @return le gestionnaire d'authentification pour le domaine auth V2
     */
    @Bean("prometheusAuthManager")
    public AuthenticationManager prometheusAuthManager(
            @Qualifier("accountV2UserDetailsService") AccountV2UserDetailsService accountV2Service,
            PasswordEncoder passwordEncoder) {
        return new WebAuthenticationManager(accountV2Service, passwordEncoder);
    }
}
