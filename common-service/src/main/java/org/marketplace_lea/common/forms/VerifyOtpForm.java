package org.marketplace_lea.common.forms;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record VerifyOtpForm(
        @NotBlank(message = "Le login ne doit pas être nul !")
        @Pattern(regexp = "^[0-9]+", message = "Veuillez entrer un numéro de téléphone composé uniquement de chiffres.")
        String login,

        @NotBlank(message = "Le code pays ne doit pas être nul !")
        String countryCode,

        @Min(value = 10000, message = "Veuiller entrer un code OTP valide.")
        int otp
) {


}
