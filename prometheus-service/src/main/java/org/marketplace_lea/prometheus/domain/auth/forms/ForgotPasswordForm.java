package org.marketplace_lea.prometheus.domain.auth.forms;

import jakarta.validation.constraints.NotBlank;

/**
 * Formulaire de demande de réinitialisation de mot de passe.
 *
 * <p>Déclenche l'envoi d'un OTP par SMS permettant à l'utilisateur
 * de réinitialiser son mot de passe.</p>
 */
public record ForgotPasswordForm(

        @NotBlank(message = "Veuillez entrer votre login.")
        String login,

        @NotBlank(message = "Veuillez sélectionner votre pays.")
        String countryCode
) {
}
