package org.marketplace_lea.prometheus.domain.auth.forms;

import jakarta.validation.constraints.NotBlank;

/**
 * Formulaire de demande d'OTP.
 *
 * <p>Déclenche l'envoi d'un code OTP par SMS au numéro de téléphone
 * associé au compte identifié par {@code login} et {@code countryCode}.</p>
 */
public record RequestOtpForm(

        @NotBlank(message = "Veuillez entrer votre login.")
        String login,

        @NotBlank(message = "Veuillez sélectionner votre pays.")
        String countryCode
) {
}
