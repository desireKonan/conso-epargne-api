package org.marketplace_lea.common.forms;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record WithdrawalForm(
        @NotNull(message = "Veuillez entrer le montant.")
        @Min(value = 100, message = "Le montant minimum autorisé est de 100 points")
        Float amount,

        @NotNull(message = "Veuillez spécifier la methode de paiment.")
        Long paymentMethodId,

        @Pattern(regexp = "^[0-9]{10}$", message = "Veuillez entrer un numéro de téléphone de 10 chiffres")
        String phoneNumber
) {
}
