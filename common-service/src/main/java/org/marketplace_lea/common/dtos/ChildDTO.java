package org.marketplace_lea.common.dtos;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.Optional;

@Data
public class ChildDTO {
    @Getter(AccessLevel.NONE)
    private String firstName;

    @Getter(AccessLevel.NONE)
    private String lastName;

    private String email;

    private List<PhoneNumberDTO> phoneNumbers;

    private boolean partner;

    public String getFirstName() {
        return Optional.of(firstName)
                .filter((fn) -> !fn.isEmpty())
                .orElse("Conso");
    }

    public String getFirstNameOld() {
        return firstName != null && !firstName.isEmpty() ?
                firstName :
                "Conso";
    }

    public String getLastName() {
        return Optional.ofNullable(lastName)
                .filter(name -> !name.trim().isEmpty())
                .orElseGet(() -> partner ? "Partenaire" : "Consom'acteur");
    }

    public String getLastNameOld() {
        return lastName != null && !lastName.isEmpty() ?
                lastName :
                (
                        partner ?
                                "Partenaire" :
                                "Consom'acteur"
                );
    }
}
