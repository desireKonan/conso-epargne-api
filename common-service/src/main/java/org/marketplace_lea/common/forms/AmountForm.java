package org.marketplace_lea.common.forms;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AmountForm(
        @NotNull(message = "Veuillez entrer un montant.")
        @Min(value = 1, message = "Veuillez entrer un montant valide.")
        Float amount
) {
}
