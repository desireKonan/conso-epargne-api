package org.marketplace_lea.prometheus.common.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationV2Form implements Serializable {
    private String parentCode;

    @Pattern(regexp = "^[0-9]+", message = "Veuillez entrer un numéro de téléphone composé uniquement de chiffre.")
    private String login;

    @NotBlank(message = "Veuillez entrer votre code pays.")
    private String countryCode;

    @Email(message = "Veuillez entrer une addresse email valide")
    @NotBlank(message = "Veuillez entrer une adresse email.")
    private String email;

    @Pattern(regexp = "^[0-9]{5}$", message = "Veuillez entrer un code à 5 chiffres.")
    private String password;

    @NotBlank(message = "Veuillez entrer le nom.")
    private String lastName;

    @NotBlank(message = "Veuillez entrer le(s) prénom(s).")
    private String firstName;

    @NotBlank(message = "Veuillez entrer votre addresse !")
    private String address;

    private String token;

    @Max(value = 90)
    @Min(value = -90)
    private float latitude;

    @Max(value = 180)
    @Min(value = -180)
    private float longitude;

    @NotBlank(message = "Veuillez spécifier le type de compte")
    private String accountTypeId;

    private String phoneNumber;

    private boolean wave;

    public BigDecimal latitude() {
        return BigDecimal.valueOf(latitude);
    }

    public BigDecimal longitude() {
        return BigDecimal.valueOf(longitude);
    }
}
