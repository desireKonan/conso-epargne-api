package org.marketplace_lea.common.common.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtilsV2 {
    public static final String DEFAULT_LOGIN = "ANONYMOUS";
    public static final String AUTHENTICATION_URI = "/ws/auth";
    public static final String AUTHORIZATION_TOKEN_PREFIX = "Bearer";
    public static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    public static final String RESET_PASSWORD_AUTHORITY = "RESET_PASSWORD";

    private SecurityUtilsV2() {
        // Classe utilitaire non instanciable
    }

    /**
     * Retourne le login de l'utilisateur connecté.
     * Supporte aussi bien un principal de type String que UserDetails.
     */
    public static String getCurrentUserLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return DEFAULT_LOGIN;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }
        if (principal instanceof String stringPrincipal) {
            return stringPrincipal;
        }
        return DEFAULT_LOGIN;
    }

    // Optionnel : méthode plus stricte qui lève une exception si aucun utilisateur
    public static String getCurrentUserLoginOrThrow() {
        String login = getCurrentUserLogin();
        if (DEFAULT_LOGIN.equals(login)) {
            throw new IllegalStateException("No authenticated user found");
        }
        return login;
    }
}
