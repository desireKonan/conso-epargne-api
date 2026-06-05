package org.marketplace_lea.prometheus.domain.auth.forms;

import jakarta.validation.constraints.NotBlank;

/**
 * Formulaire de mise à jour du token FCM (Firebase Cloud Messaging).
 *
 * <p>Permet à l'application mobile d'enregistrer ou mettre à jour
 * le token de notification push associé au compte.</p>
 */
public record UpdateFcmTokenForm(

        @NotBlank(message = "Veuillez entrer votre login.")
        String login,

        @NotBlank(message = "Veuillez entrer le token FCM.")
        String fcmToken
) {
}
