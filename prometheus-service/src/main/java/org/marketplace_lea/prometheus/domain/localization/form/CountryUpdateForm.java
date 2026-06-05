package org.marketplace_lea.prometheus.domain.localization.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountryUpdateForm {

    @NotBlank(message = "Le label est obligatoire")
    private String label;

    @NotBlank(message = "Le code est obligatoire")
    private String code;

    @NotBlank(message = "L'indicatif téléphonique est obligatoire")
    private String callingCode;

    @Positive(message = "La longueur doit être positive")
    private Integer phoneNumberLength;

    private Boolean enabled;
}