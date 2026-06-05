package org.marketplace_lea.common.forms;

import org.marketplace_lea.common.dtos.CollectRecipientDTO;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public record CollectForm(
        @NotBlank(message = "Veuillez entrer le libellé de la collecte !")
        String label,

        @NotNull(message = "Veuillez indiquer le type de projet !")
        String projectTypeId,

        boolean partnerOnly,

        boolean code,

        @NotEmpty(message = "Veuillez entrer la description du projet !")
        String description,

        @Min(value = 0)
        @Max(value = 100)
        float counterparty,

        @NotBlank(message = "Veuillez ajouter une image à la collecte !")
        String image,

        String videoUrl,

        @Size(min = 1, max = 4, message = "Le nombre de signataire doit être compris entre 1 et 4 inclus")
        List<CollectRecipientDTO> collectRecipients,

        Date startDate,

        // @Min(value = 200000, message = "Le montant de la collecte doit être supérieur ou égal à 200000 FCFA")
        BigDecimal amount,

        @Min(value = 1, message = "La durée minimale d'une collecte est d'un (1) mois")
        @Max(value = 6, message = "La durée de la collecte doit être inférieure ou égale à six (6) mois")
        int duration

) {

    public boolean isCode() {
        return code;
    }
}
