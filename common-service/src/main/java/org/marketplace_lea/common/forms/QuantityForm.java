package org.marketplace_lea.common.forms;

import jakarta.validation.constraints.Min;

public record QuantityForm(
        @Min(value = 0, message = "Veuillez entrer une quantité supérieure ou égale à un (1).")
        int quantity
) {
}
