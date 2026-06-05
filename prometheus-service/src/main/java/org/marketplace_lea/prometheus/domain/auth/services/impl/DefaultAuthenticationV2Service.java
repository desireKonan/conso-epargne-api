package org.marketplace_lea.prometheus.domain.auth.services.impl;

import org.marketplace_lea.common.common.dtos.UserPrincipalV2;
import org.marketplace_lea.common.common.exceptions.ConsoEpargneException;
import org.marketplace_lea.common.common.service.jwt.JwtTokenService;
import org.marketplace_lea.common.common.service.notification.SmsService;
import org.marketplace_lea.common.dtos.CustomerTokenInfo;
import org.marketplace_lea.common.dtos.OtpResponse;
import org.marketplace_lea.common.entities.account.AccountV2Entity;
import org.marketplace_lea.common.entities.customer.CustomerV2Entity;
import org.marketplace_lea.common.forms.VerifyOtpForm;
import org.marketplace_lea.common.mapper.CustomerV2Mapper;
import org.marketplace_lea.common.repositories.account.AccountV2JpaRepository;
import org.marketplace_lea.common.repositories.customer.CustomerV2JpaRepository;
import org.marketplace_lea.common.services.OTPService;
import org.marketplace_lea.prometheus.domain.auth.dtos.AuthTokenV2Response;
import org.marketplace_lea.prometheus.domain.auth.forms.ForgotPasswordForm;
import org.marketplace_lea.prometheus.domain.auth.forms.LoginV2Form;
import org.marketplace_lea.prometheus.domain.auth.forms.RequestOtpForm;
import org.marketplace_lea.prometheus.domain.auth.forms.ResetPasswordForm;
import org.marketplace_lea.prometheus.domain.auth.forms.UpdateFcmTokenForm;
import org.marketplace_lea.prometheus.domain.auth.services.AuthenticationV2Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Implémentation du service d'authentification V2.
 *
 * <p>Gère le cycle complet d'authentification : login JWT via {@link AuthenticationManager},
 * OTP SMS, réinitialisation de mot de passe et gestion du compte.</p>
 *
 * <p>La méthode {@link #login(LoginV2Form)} délègue l'authentification au bean
 * {@code "prometheusAuthManager"} ({@link org.marketplace_lea.common.common.service.auth.WebAuthenticationManager}),
 * répliquant exactement la logique de {@code AuthenticationFacade.authenticate()} :
 * chargement du compte via {@code AccountService}, vérification BCrypt et état du compte.</p>
 */
@Slf4j
@Service
public class DefaultAuthenticationV2Service implements AuthenticationV2Service {
    private static final String SMS_SENDER_ID = "EDISPRO";
    private static final String OTP_SMS_TEMPLATE = "Votre code de vérification est : %s";
    private static final String ROLE_USER = "ROLE_USER";

    private final AccountV2JpaRepository accountRepository;
    private final CustomerV2JpaRepository customerRepository;
    private final CustomerV2Mapper customerV2Mapper;
    private final JwtTokenService jwtTokenService;
    private final OTPService otpService;
    private final SmsService smsService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Value("${app.authentication.country-based:disabled}")
    private String countryBasedAuthentication;

    public DefaultAuthenticationV2Service(AccountV2JpaRepository accountRepository, CustomerV2JpaRepository customerRepository, CustomerV2Mapper customerV2Mapper, JwtTokenService jwtTokenService, OTPService otpService, SmsService smsService, PasswordEncoder passwordEncoder, @Qualifier("prometheusAuthManager") AuthenticationManager authenticationManager) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.customerV2Mapper = customerV2Mapper;
        this.jwtTokenService = jwtTokenService;
        this.otpService = otpService;
        this.smsService = smsService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    // ─────────────────────────────────────────────
    // Connexion
    // ─────────────────────────────────────────────

    /**
     * {@inheritDoc}
     *
     * <p>Délègue l'authentification (chargement du compte, vérification BCrypt,
     * état verrouillé/désactivé) au {@link AuthenticationManager} configuré,
     * de façon identique à {@code AuthenticationFacade.authenticate()}.</p>
     */
    @Override
    @Transactional(readOnly = true)
    public AuthTokenV2Response login(LoginV2Form form) {
        log.info("[AuthV2.login] Tentative de connexion pour le login: {}", form.login());

        // 1. Délégation de l'authentification au WebAuthenticationManager
        //    → loadUserByUsername(formattedLogin) + vérification BCrypt + état du compte
        Authentication authentication = authenticateOrThrow(form);
        UserPrincipalV2 principal = (UserPrincipalV2) authentication.getPrincipal();
        AccountV2Entity account = principal.getAccount();

        // 2. Vérification de la suppression (soft delete via deletedAt)
        if (account.getDeletedAt() != null) {
            log.warn("[AuthV2.login] Compte supprimé pour: {}", form.login());
            throw new ConsoEpargneException("Ce compte a été supprimé.", HttpStatus.FORBIDDEN);
        }

        // 3. Récupération du client V2 associé au login
        CustomerV2Entity customer = resolveCustomerByAccountLogin(account.getLogin());
        var customerDTO = customerV2Mapper.toInfo(customer);

        // 4. Génération du JWT avec les claims du compte V2 et le profil client V2
        String token = jwtTokenService.generateToken(
                buildTokenClaims(account.getLogin(), account.getCountryCode(), customerDTO));
        Instant expireAt = Instant.now().plusMillis(getDefaultTokenExpirationMs());

        log.info("[AuthV2.login] Connexion réussie pour: {}", form.login());
        return AuthTokenV2Response.builder()
                .accessToken(token)
                .expireAt(expireAt)
                .customer(customerDTO)
                .build();
    }

    // ─────────────────────────────────────────────
    // OTP
    // ─────────────────────────────────────────────

    /**
     * {@inheritDoc}
     */
    @Override
    public OtpResponse requestOtp(RequestOtpForm form) {
        log.info("[AuthV2.requestOtp] Demande OTP pour: {}", form.login());

        AccountV2Entity account = findActiveAccountV2OrThrow(form.login(), form.countryCode());

        // Génération de l'OTP et envoi par SMS
        int otp = otpService.generateOTP(form.login());
        String phoneRecipient = buildPhoneRecipient(account.getLogin());
        smsService.sendSms(SMS_SENDER_ID, String.format(OTP_SMS_TEMPLATE, otp), phoneRecipient);
        log.info("[AuthV2.requestOtp] OTP généré et SMS envoyé pour: {}", form.login());
        return OtpResponse.builder()
                .uuid(form.login())
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean verifyOtp(VerifyOtpForm form) {
        log.info("[AuthV2.verifyOtp] Vérification OTP pour: {}", form.login());
        boolean isValid = otpService.isValidOtp(form.login(), form.otp());

        if (isValid) {
            otpService.clearOTP(form.login());
            log.info("[AuthV2.verifyOtp] OTP valide et consommé pour: {}", form.login());
        } else {
            log.warn("[AuthV2.verifyOtp] OTP invalide pour: {}", form.login());
        }

        return isValid;
    }

    // ─────────────────────────────────────────────
    // Mot de passe
    // ─────────────────────────────────────────────

    /**
     * {@inheritDoc}
     */
    @Override
    public void forgotPassword(ForgotPasswordForm form) {
        log.info("[AuthV2.forgotPassword] Demande reset mot de passe pour: {}", form.login());

        AccountV2Entity account = findActiveAccountV2OrThrow(form.login(), form.countryCode());

        int otp = otpService.generateOTP(form.login());
        String phoneRecipient = buildPhoneRecipient(account.getLogin());
        smsService.sendSms(SMS_SENDER_ID, String.format(OTP_SMS_TEMPLATE, otp), phoneRecipient);

        log.info("[AuthV2.forgotPassword] OTP de reset envoyé pour: {}", form.login());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void resetPassword(ResetPasswordForm form) {
        log.info("[AuthV2.resetPassword] Réinitialisation du mot de passe pour: {}", form.login());

        // 1. Vérification de l'OTP
        if (!otpService.isValidOtp(form.login(), form.otp())) {
            throw new ConsoEpargneException("Le code OTP est invalide ou expiré.", HttpStatus.BAD_REQUEST);
        }

        otpService.clearOTP(form.login());

        // 2. Récupération et mise à jour du compte V2
        AccountV2Entity account = findActiveAccountV2OrThrow(form.login(), form.countryCode());
        account.setPassword(passwordEncoder.encode(form.newPassword()));
        accountRepository.save(account);

        log.info("[AuthV2.resetPassword] Mot de passe mis à jour pour: {}", form.login());
    }

    // ─────────────────────────────────────────────
    // Compte
    // ─────────────────────────────────────────────

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteAccount(String login) {
        log.info("[AuthV2.deleteAccount] Suppression du compte: {}", login);

        CustomerV2Entity customer = customerRepository.getByLogin(login)
                .orElseThrow(() -> new ConsoEpargneException("Aucun compte trouvé pour le login : " + login, HttpStatus.NOT_FOUND));

        // Soft delete : on marque la date de suppression et on blackliste le compte
        customer.setDeletedAt(LocalDateTime.now());
        customer.getAccount().setBlacklisted(true);
        customerRepository.save(customer);

        log.info("[AuthV2.deleteAccount] Compte supprimé (soft) pour: {}", login);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void updateFcmToken(UpdateFcmTokenForm form) {
        log.info("[AuthV2.updateFcmToken] Mise à jour FCM pour: {}", form.login());

        AccountV2Entity account = accountRepository.findAccountV2ByLogin(form.login())
                .orElseThrow(() -> new ConsoEpargneException("Aucun compte trouvé pour le login : " + form.login(), HttpStatus.NOT_FOUND));

        account.setNotificationToken(form.fcmToken());
        accountRepository.save(account);

        log.info("[AuthV2.updateFcmToken] Token FCM mis à jour pour: {}", form.login());
    }

    // ─────────────────────────────────────────────
    // Méthodes privées utilitaires
    // ─────────────────────────────────────────────

    /**
     * Délègue l'authentification au {@link AuthenticationManager} et traduit
     * les exceptions Spring Security en {@link ConsoEpargneException} HTTP.
     */
    private Authentication authenticateOrThrow(LoginV2Form form) {
        try {
            String login = "disabled".equals(countryBasedAuthentication) ? form.login() : form.formattedLogin();
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(login, form.password())
            );
        } catch (AuthenticationException ex) {
            log.warn("[AuthV2.login] Échec d'authentification pour '{}' : {}", form.login(), ex.getMessage());
            throw new ConsoEpargneException(ex.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Recherche un compte V2 actif (non supprimé) par login et code pays.
     * Utilisé pour les opérations OTP, reset password et forgot password
     * qui ne passent pas par le {@link AuthenticationManager}.
     */
    private AccountV2Entity findActiveAccountV2OrThrow(String login, String countryCode) {
        return accountRepository.getByActiveLogin(login)
                .filter(acc -> countryCode == null || countryCode.isBlank() || countryCode.equalsIgnoreCase(acc.getCountryCode()))
                .orElseThrow(() -> new ConsoEpargneException("Aucun compte actif trouvé pour le login : " + login, HttpStatus.NOT_FOUND));
    }

    /**
     * Récupère le client V2 associé au login du compte.
     */
    private CustomerV2Entity resolveCustomerByAccountLogin(String login) {
        return customerRepository.getByLogin(login)
                .orElseThrow(() -> new ConsoEpargneException("Profil client V2 introuvable pour le login : " + login, HttpStatus.NOT_FOUND));
    }

    /**
     * Construit les claims JWT à partir du login et du code pays du compte V1
     * (extraits du {@link UserPrincipalV2}) et du profil client V2.
     */
    private Map<String, Object> buildTokenClaims(String login, String countryCode, CustomerTokenInfo customerDTO) {
        return Map.of(
                "username", login,
                "roles", List.of(ROLE_USER),
                "country-code", countryCode != null ? countryCode : "",
                "customer", customerDTO
        );
    }

    /**
     * Retourne la durée d'expiration par défaut du token (24h en ms).
     * Valeur cohérente avec la configuration JWT de l'application principale.
     */
    private long getDefaultTokenExpirationMs() {
        return 24L * 3600 * 1000;
    }

    /**
     * Construit le numéro de téléphone international pour l'envoi SMS.
     */
    private String buildPhoneRecipient(String login) {
        boolean isCountryBased = "enabled".equalsIgnoreCase(countryBasedAuthentication);
        if (isCountryBased) {
            return login;
        }
        return String.format("+225%s", login);
    }
}
