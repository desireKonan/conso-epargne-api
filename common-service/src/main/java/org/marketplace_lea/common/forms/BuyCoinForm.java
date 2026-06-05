package org.marketplace_lea.common.forms;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record BuyCoinForm(
        long paymentMethodId,

        @Pattern(regexp = "^[0-9]{10}$", message = "Veuillez entrer un numéro de téléphone de 10 chiffres")
        String transactionPhoneNumber,

        @NotNull(message = "Veuillez entrer un montant")
        @Min(value = 1, message = "Le montant minimum est de 1 Léa")
        Float amount,
        float coinAmount
) {

}