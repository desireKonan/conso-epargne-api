package org.marketplace_lea.prometheus.domain.auth.controllers;

import org.marketplace_lea.common.common.constants.Router;
import org.marketplace_lea.common.dtos.OtpResponse;
import org.marketplace_lea.common.forms.VerifyOtpForm;
import org.marketplace_lea.prometheus.domain.auth.dtos.AuthTokenV2Response;
import org.marketplace_lea.prometheus.domain.auth.forms.ForgotPasswordForm;
import org.marketplace_lea.prometheus.domain.auth.forms.LoginV2Form;
import org.marketplace_lea.prometheus.domain.auth.forms.RequestOtpForm;
import org.marketplace_lea.prometheus.domain.auth.forms.ResetPasswordForm;
import org.marketplace_lea.prometheus.domain.auth.forms.UpdateFcmTokenForm;
import org.marketplace_lea.prometheus.domain.auth.services.AuthenticationV2Service;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur REST d'authentification V2.
 *
 * <p>Expose les endpoints du cycle d'authentification client :
 * connexion, OTP, réinitialisation de mot de passe, suppression de compte
 * et mise à jour du token FCM.</p>
 *
 * <p>Base URI : {@code /api/v1/authenticate/v2}</p>
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(Router.API_AUTHENTICATION_URI)
public class AuthApiV2Controller {

    private final AuthenticationV2Service authService;

    // ─────────────────────────────────────────────
    // Connexion
    // ─────────────────────────────────────────────

    /**
     * Authentifie un client et retourne un token JWT.
     *
     * <pre>POST /api/v1/authenticate/v2</pre>
     *
     * @param form formulaire de connexion
     * @return 200 avec le token d'accès et les informations client
     */
    @PostMapping()
    public ResponseEntity<AuthTokenV2Response> login(@Valid @RequestBody LoginV2Form form) {
        log.info("[AuthApiV2] Connexion pour le login: {}", form.login());
        AuthTokenV2Response response = authService.login(form);
        return ResponseEntity.ok(response);
    }

    // ─────────────────────────────────────────────
    // OTP
    // ─────────────────────────────────────────────

    /**
     * Génère et envoie un OTP par SMS au client identifié.
     *
     * <pre>POST /api/v1/authenticate/v2/otp</pre>
     *
     * @param form formulaire avec le login et le code pays
     * @return 200 avec les métadonnées OTP
     */
    @PostMapping("/otp")
    public ResponseEntity<OtpResponse> requestOtp(@Valid @RequestBody RequestOtpForm form) {
        log.info("[AuthApiV2] Demande OTP pour: {}", form.login());
        OtpResponse response = authService.requestOtp(form);
        return ResponseEntity.ok(response);
    }

    /**
     * Vérifie la validité d'un OTP.
     *
     * <pre>POST /api/v1/authenticate/v2/verify-otp</pre>
     *
     * @param form formulaire avec le login, le code pays et l'OTP
     * @return 200 avec {@code true} si l'OTP est valide, {@code false} sinon
     */
    @PostMapping("/verify-otp")
    public ResponseEntity<Boolean> verifyOtp(@Valid @RequestBody VerifyOtpForm form) {
        log.info("[AuthApiV2] Vérification OTP pour: {}", form.login());
        boolean isValid = authService.verifyOtp(form);
        return ResponseEntity.ok(isValid);
    }

    // ─────────────────────────────────────────────
    // Mot de passe
    // ─────────────────────────────────────────────

    /**
     * Déclenche l'envoi d'un OTP SMS pour la réinitialisation du mot de passe.
     *
     * <pre>POST /api/v1/authenticate/v2/forgot-password</pre>
     *
     * @param form formulaire avec le login et le code pays
     * @return 204 No Content
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@Valid @RequestBody ForgotPasswordForm form) {
        log.info("[AuthApiV2] Demande reset password pour: {}", form.login());
        authService.forgotPassword(form);
        return ResponseEntity.noContent().build();
    }

    /**
     * Réinitialise le mot de passe après vérification de l'OTP.
     *
     * <pre>POST /api/v1/authenticate/v2/reset-password</pre>
     *
     * @param form formulaire avec le login, le code pays, l'OTP et le nouveau mot de passe
     * @return 204 No Content
     */
    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordForm form) {
        log.info("[AuthApiV2] Réinitialisation mot de passe pour: {}", form.login());
        authService.resetPassword(form);
        return ResponseEntity.noContent().build();
    }

    // ─────────────────────────────────────────────
    // Compte
    // ─────────────────────────────────────────────

    /**
     * Supprime le compte du client authentifié (soft delete).
     *
     * <pre>DELETE /api/v1/authenticate/v2/delete-account?login={login}</pre>
     *
     * @param login login du compte à supprimer
     * @return 204 No Content
     */
    @DeleteMapping("/delete-account")
    public ResponseEntity<Void> deleteAccount(@RequestParam String login) {
        log.info("[AuthApiV2] Suppression du compte: {}", login);
        authService.deleteAccount(login);
        return ResponseEntity.noContent().build();
    }

    /**
     * Met à jour le token FCM (Firebase Cloud Messaging) du compte.
     *
     * <pre>PUT /api/v1/authenticate/v2/fcm-token</pre>
     *
     * @param form formulaire avec le login et le nouveau token FCM
     * @return 204 No Content
     */
    @PutMapping("/fcm-token")
    public ResponseEntity<Void> updateFcmToken(@Valid @RequestBody UpdateFcmTokenForm form) {
        log.info("[AuthApiV2] Mise à jour FCM pour: {}", form.login());
        authService.updateFcmToken(form);
        return ResponseEntity.noContent().build();
    }
}
