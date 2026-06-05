package org.marketplace_lea.common.forms;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DonationForm(
        @Min(value = 100, message = "Le montant minimum est de 100")
        float amount,

        @NotNull
        @NotBlank(message = "Veuillez entrer un numéro de téléphone valide pour le paiement !")
        String phoneNumber,
        boolean onlinePayment,
        boolean wave
) {

}
