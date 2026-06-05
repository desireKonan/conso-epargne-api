package org.marketplace_lea.prometheus.domain.auth.services;

import org.marketplace_lea.common.dtos.OtpResponse;
import org.marketplace_lea.common.forms.VerifyOtpForm;
import org.marketplace_lea.prometheus.domain.auth.dtos.AuthTokenV2Response;
import org.marketplace_lea.prometheus.domain.auth.forms.ForgotPasswordForm;
import org.marketplace_lea.prometheus.domain.auth.forms.LoginV2Form;
import org.marketplace_lea.prometheus.domain.auth.forms.RequestOtpForm;
import org.marketplace_lea.prometheus.domain.auth.forms.ResetPasswordForm;
import org.marketplace_lea.prometheus.domain.auth.forms.UpdateFcmTokenForm;

/**
 * Contrat de service pour l'authentification V2.
 *
 * <p>Ce service expose toutes les opérations liées au cycle d'authentification
 * d'un client : connexion, OTP, réinitialisation de mot de passe, suppression de compte
 * et gestion du token FCM.</p>
 *
 * <p>Il respecte le principe de ségrégation des interfaces (ISP) et
 * d'inversion des dépendances (DIP).</p>
 */
public interface AuthenticationV2Service {

    // ─────────────────────────────────────────────
    // Connexion
    // ─────────────────────────────────────────────

    /**
     * Authentifie un client et génère un token JWT.
     *
     * <p>Vérifie le login, le mot de passe et l'état du compte
     * (non blacklisté, non supprimé). Retourne un token d'accès
     * ainsi que les informations publiques du client.</p>
     *
     * @param form formulaire de connexion (login + password + countryCode optionnel)
     * @return la réponse d'authentification avec le JWT et les données client
     */
    AuthTokenV2Response login(LoginV2Form form);

    // ─────────────────────────────────────────────
    // OTP
    // ─────────────────────────────────────────────

    /**
     * Génère et envoie un OTP par SMS au client identifié.
     *
     * <p>Si un OTP existant est en cache pour ce login, il est
     * invalidé avant d'en générer un nouveau.</p>
     *
     * @param form formulaire contenant le login et le code pays
     * @return les métadonnées de l'OTP généré (sans exposer le code lui-même)
     */
    OtpResponse requestOtp(RequestOtpForm form);

    /**
     * Vérifie la validité d'un OTP pour un login donné.
     *
     * @param form formulaire contenant le login, le code pays et l'OTP saisi
     * @return {@code true} si l'OTP est valide, {@code false} sinon
     */
    boolean verifyOtp(VerifyOtpForm form);

    // ─────────────────────────────────────────────
    // Mot de passe
    // ─────────────────────────────────────────────

    /**
     * Déclenche la procédure de réinitialisation de mot de passe.
     *
     * <p>Génère un OTP et l'envoie par SMS au client identifié par son login.
     * L'OTP doit ensuite être soumis via {@link #resetPassword(ResetPasswordForm)}.</p>
     *
     * @param form formulaire contenant le login et le code pays
     */
    void forgotPassword(ForgotPasswordForm form);

    /**
     * Réinitialise le mot de passe après vérification de l'OTP.
     *
     * <p>Valide l'OTP, puis met à jour le mot de passe du compte
     * avec la valeur encodée du nouveau mot de passe fourni.</p>
     *
     * @param form formulaire contenant le login, le code pays, l'OTP et le nouveau mot de passe
     */
    void resetPassword(ResetPasswordForm form);

    // ─────────────────────────────────────────────
    // Compte
    // ─────────────────────────────────────────────

    /**
     * Supprime le compte du client identifié par son login (soft delete).
     *
     * <p>Positionne la date de suppression ({@code deletedAt}) sur le client
     * et invalide son compte (blacklisting) pour empêcher toute connexion future.</p>
     *
     * @param login login du compte à supprimer
     */
    void deleteAccount(String login);

    /**
     * Met à jour le token FCM (Firebase Cloud Messaging) du compte.
     *
     * @param form formulaire contenant le login et le nouveau token FCM
     */
    void updateFcmToken(UpdateFcmTokenForm form);
}
