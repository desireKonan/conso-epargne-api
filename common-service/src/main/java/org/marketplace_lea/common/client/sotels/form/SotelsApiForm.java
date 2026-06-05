package org.marketplace_lea.common.client.sotels.form;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Data
@ToString
@NoArgsConstructor
public class SotelsApiForm {
    private int amount;

    @Pattern(regexp = "^[0-9]{10}$", message = "Veuillez entrer le muméro de téléphone valide svp !")
    private String msisdn;

    private String otp;

    @NotBlank(message = "Veuillez sélectionner une methode de paiement !")
    private String name;

    private String type = "mobile_money";

    private String reference;

    public SotelsApiForm(int amount) {
        this.amount = amount;
    }

}
