package org.marketplace_lea.common.forms;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ResetPaswordForm(
        @NotNull(message = "Veuillez entrer un code svp.")
        @Pattern(regexp = "^[0-9]{5}$", message = "Veuillez entrer un code à 5 chiffres.")
        String newPassword
) {
}
