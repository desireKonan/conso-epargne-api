package org.marketplace_lea.common.common.domain;

import jakarta.validation.constraints.NotBlank;

public record AuthDetailsForm(
        @NotBlank(message = "Veuillez entrer le login !")
        String login,

        @NotBlank(message = "Veuillez entrer le mot de passe !")
        String password,

        @NotBlank(message = "Veuillez sélectionner le pays !")
        String countryCode
) {
    public String getFormattedLogin() {
        return String.format("%s.%s", login, countryCode);
    }
}
