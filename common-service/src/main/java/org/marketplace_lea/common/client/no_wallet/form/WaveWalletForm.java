package org.marketplace_lea.common.client.no_wallet.form;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // Ignore les champs null dans le JSON
public record WaveWalletForm(
        @Min(value = 50, message = "Le montant ne doit pas être inférieur à '50'")
        int amount,

        @NotBlank(message = "Le numéro de téléphone ne doit pas être null !")
        String customerPhone,

        @NotBlank(message = "Le numéro de téléphone ne doit pas être null !")
        String initiatorId
) {
}
