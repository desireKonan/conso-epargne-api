package org.marketplace_lea.common.common.utils;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class SecurityUtils {

    /**
     * Default login when no user is logged in.
     */
    public static final String DEFAULT_LOGIN = "ANONYMOUS";
    public static final String AUTHENTICATION_URI = "/ws/auth";
    public static final String AUTHORIZATION_TOKEN_PREFIX = "Bearer";
    public static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    public static final String RESET_PASSWORD_AUTHORITY = "RESET_PASSWORD";

    private static final String SECRET = "consoEp@rgneSecretKey";

    public static String getConnectedCustomerLogin() {
        SecurityContext context = SecurityContextHolder.getContext();
        String login = DEFAULT_LOGIN;

        if (context != null && context.getAuthentication() != null) {
            login = (String) context.getAuthentication()
                    .getPrincipal();
        }

        return login;
    }

    public static String getConnectedCustomerLoginv2() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map((authentication)  -> (String) authentication.getPrincipal())
                .orElse(DEFAULT_LOGIN);
    }
}
