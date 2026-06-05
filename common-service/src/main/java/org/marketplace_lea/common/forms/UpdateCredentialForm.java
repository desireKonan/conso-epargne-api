package org.marketplace_lea.common.forms;

import jakarta.validation.constraints.NotBlank;

public record UpdateCredentialForm(
        @NotBlank(message = "Veuillez renseigner l'ancien mot de passe.")
        String oldPassword,

        @NotBlank(message = "Veuillez renseigner le nouveau mot de passe.")
        String newPassword
) {

}
