package org.marketplace_lea.common.client.paystack.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record InitializeTransactionForm(
        @Email(message = "Email invalide")
        @NotBlank(message = "L'email est requis")
        String email,

        @Min(value = 100, message = "Le montant minimum est 100 (1 XOF)")
        String amount
) {
}
