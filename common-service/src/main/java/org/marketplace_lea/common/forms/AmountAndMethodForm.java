package org.marketplace_lea.common.forms;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AmountAndMethodForm(
        @NotNull(message = "Veuillez entrer un montant.")
        @Min(value = 1, message = "Veuillez entrer un montant valide.")
        Float amount,

        @NotBlank(message = "Veuillez entrer un numéro de téléphone valide pour le paiement !")
        String phoneNumber,
        boolean wave,

        @NotBlank(message = "Le code parent ne doit pas être nul !")
        String leaParentCode
) {
}
