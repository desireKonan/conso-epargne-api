package org.marketplace_lea.common.forms;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationForm implements Serializable {
    @NotBlank(message = "Veuillez spécifier un code de parrainage")
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

    @Valid
    private AddressForm address;

    // private List<String> phoneNumbers =  new ArrayList<>();

    @NotBlank(message = "Veuillez spécifier le type de compte")
    private String accountTypeId;

    private String phoneNumber;

    private boolean wave;


    public boolean hasAddress() {
        return address.districtId() == null || address.districtId().isBlank();
    }
}
