package org.marketplace_lea.prometheus.domain.auth.forms;

import jakarta.validation.constraints.NotBlank;

/**
 * Formulaire de connexion V2.
 *
 * <p>Supporte deux modes d'authentification :
 * <ul>
 *   <li><b>Simple</b> : login + password uniquement (pays = "CIV" par défaut)</li>
 *   <li><b>Country-based</b> : login + password + countryCode (activé via {@code app.authentication.country-based=enabled})</li>
 * </ul>
 * </p>
 */
public record LoginV2Form(

        @NotBlank(message = "Veuillez entrer votre login.")
        String login,

        @NotBlank(message = "Veuillez entrer votre mot de passe.")
        String password,

        /**
         * Code pays ISO alpha-3 (ex: "CIV", "SEN").
         * Utilisé uniquement si l'authentification basée sur le pays est activée.
         * Peut être null en mode simple.
         */
        String countryCode
) {
    /**
     * Retourne le login formaté pour la recherche en base.
     * En mode country-based, le login est stocké sous la forme "login.countryCode".
     */
    public String formattedLogin() {
        if (countryCode != null && !countryCode.isBlank()) {
            return String.format("%s.%s", login, countryCode);
        }
        return login;
    }
}
