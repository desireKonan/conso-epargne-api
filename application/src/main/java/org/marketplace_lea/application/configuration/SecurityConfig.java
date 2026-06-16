package org.marketplace_lea.application.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.marketplace_lea.application.configuration.filter.JwtAuthorizationFilter;
import org.marketplace_lea.common.common.service.auth.WebAuthenticationManager;
import org.marketplace_lea.common.common.service.jwt.JwtTokenService;
import org.marketplace_lea.common.services.account.AccountV2UserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.marketplace_lea.common.common.constants.Router.API_AUTHENTICATION_URI;
import static org.marketplace_lea.common.common.constants.Router.API_REGISTRATION_URI;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final AccountV2UserDetailsService accountService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    // ============================== POINTS COMMUNS ==============================

    private static final String[] SWAGGER_WHITELIST = {
            "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/v2/api-docs/**",
            "/webjars/**", "/swagger-resources/**", "/swagger-ui/index.html",
            "/configuration/ui", "/configuration/security"
    };

    
    public static final String[] PUBLIC_V2_ENDPOINTS = {
            API_AUTHENTICATION_URI,
            API_REGISTRATION_URI,
            "/otp",
            "/verify-otp",
            "/forgot-password",
            "/reset-password",
            "/delete-account",
            "/fcm-token"
    };

    public SecurityConfig(AccountV2UserDetailsService accountService, BCryptPasswordEncoder passwordEncoder, JwtTokenService jwtTokenService, ObjectMapper objectMapper) {
        this.accountService = accountService;
        this.passwordEncoder = passwordEncoder;
        this.jwtAuthorizationFilter = new JwtAuthorizationFilter(jwtTokenService, objectMapper);
    }

    // ============================== PROMETHEUS V2 (STATELESS, PUBLIC AUTH ENDPOINTS) ==============================

    /**
     * Chaîne de sécurité dédiée aux endpoints d'authentification V2 du prometheus-service.
     *
     * <p>Prioritaire ({@code @Order(0)}) sur la chaîne API générale pour intercepter
     * toutes les requêtes sous {@code /api/v1/authenticate/v2/**} avant elles.<br>
     * Tous ces endpoints sont publics car ils constituent l'entrée d'authentification elle-même
     * (login, OTP, reset password, etc.).</p>
     *
     * <p>Le {@link WebAuthenticationManager} est utilisé comme gestionnaire d'authentification,
     * répliquant exactement la logique du {@code "ApiManager"} existant.</p>
     */
    @Bean
    public SecurityFilterChain prometheusApiSecurityFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher(contextPath)
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(SWAGGER_WHITELIST).permitAll()
                        .requestMatchers(PUBLIC_V2_ENDPOINTS).permitAll()
                        .anyRequest().authenticated()
                )
                .authenticationManager(prometheusAuthManager())
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Crée un {@link WebAuthenticationManager} pour les endpoints prometheus V2.
     *
     * <p>Utilise {@link AccountV2UserDetailsService} (qui implémente {@link org.springframework.security.core.userdetails.UserDetailsService})
     * et l'encodeur BCrypt — même configuration que le {@code DaoAuthenticationProvider}
     * du bean {@code "ApiManager"}.</p>
     */
    @Bean("PrometheusAuthManager")
    public AuthenticationManager prometheusAuthManager() {
        return new WebAuthenticationManager(accountService, passwordEncoder);
    }
}
