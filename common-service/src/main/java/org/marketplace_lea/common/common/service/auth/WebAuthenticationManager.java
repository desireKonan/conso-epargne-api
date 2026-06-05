package org.marketplace_lea.common.common.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * Gestionnaire d'authentification pour l'API REST (stateless, JWT).
 *
 * <p>Réplique la logique du {@code DaoAuthenticationProvider} configuré dans
 * {@code SecurityConfig} sous le bean {@code "ApiManager"} :
 * <ol>
 *   <li>Charge le {@link org.marketplace_lea.common.common.dtos.UserPrincipalV2} via {@link UserDetailsService#loadUserByUsername(String)}
 *       (implémenté par {@code AccountService})</li>
 *   <li>Vérifie que le compte n'est pas verrouillé ni désactivé</li>
 *   <li>Contrôle le mot de passe avec {@link PasswordEncoder#matches(CharSequence, String)}</li>
 *   <li>Retourne un {@link UsernamePasswordAuthenticationToken} pleinement authentifié</li>
 * </ol>
 * </p>
 *
 * <p>Le {@code username} passé dans le token d'entrée est le login formaté
 * ({@code "login.countryCode"} en mode country-based, ou simplement {@code "login"}),
 * tel que produit par {@code AuthDetailsForm.getFormattedLogin()}.</p>
 */
@Slf4j
@RequiredArgsConstructor
public class WebAuthenticationManager implements AuthenticationManager {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    /**
     * {@inheritDoc}
     *
     * @throws UsernameNotFoundException si aucun compte ne correspond au login
     * @throws LockedException           si le compte est verrouillé (blacklisté)
     * @throws DisabledException         si le compte est désactivé
     * @throws BadCredentialsException   si le mot de passe est incorrect
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String formattedLogin = (String) authentication.getPrincipal();
        String rawPassword = (String) authentication.getCredentials();

        log.debug("[WebAuthenticationManager] Tentative d'authentification pour le login: {}", formattedLogin);

        // 1. Chargement du compte — compatible V1 (UserPrincipal) et V2 (UserPrincipalV2)
        UserDetails userDetails = userDetailsService.loadUserByUsername(formattedLogin);

        // 2. Vérification de l'état du compte
        if (!userDetails.isAccountNonLocked()) {
            log.warn("[WebAuthenticationManager] Compte verrouillé: {}", formattedLogin);
            throw new LockedException("Ce compte est verrouillé.");
        }

        if (!userDetails.isEnabled()) {
            log.warn("[WebAuthenticationManager] Compte désactivé: {}", formattedLogin);
            throw new DisabledException("Ce compte est désactivé.");
        }

        // 3. Vérification du mot de passe
        if (!passwordEncoder.matches(rawPassword, userDetails.getPassword())) {
            log.warn("[WebAuthenticationManager] Mot de passe incorrect pour: {}", formattedLogin);
            throw new BadCredentialsException("Login ou mot de passe incorrect.");
        }

        log.debug("[WebAuthenticationManager] Authentification réussie pour: {}", formattedLogin);

        // 4. Retourne un token pleinement authentifié (credentials nullifiés)
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}

