package org.marketplace_lea.prometheus.domain.auth.forms;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Formulaire de réinitialisation de mot de passe.
 *
 * <p>Exige la vérification de l'OTP reçu par SMS avant d'appliquer
 * le nouveau mot de passe.</p>
 */
public record ResetPasswordForm(

        @NotBlank(message = "Veuillez entrer votre login.")
        String login,

        @NotBlank(message = "Veuillez sélectionner votre pays.")
        String countryCode,

        @Min(value = 10000, message = "Veuillez entrer un code OTP valide.")
        int otp,

        @NotBlank(message = "Veuillez entrer votre nouveau mot de passe.")
        @Size(min = 4, message = "Le mot de passe doit comporter au moins 4 caractères.")
        String newPassword
) {
}
