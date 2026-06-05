package org.marketplace_lea.prometheus.domain.customer.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO représentant un filleul (enfant) dans le réseau V2.
 *
 * <p>
 * Contient les informations de base d'un filleul avec son statut
 * (partenaire ou consomm'acteur).
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChildV2DTO {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private List<String> phoneNumbers;
    private boolean partner;

    /**
     * Retourne le prénom du filleul avec une valeur par défaut.
     */
    public String getFirstName() {
        return firstName != null && !firstName.isBlank() ? firstName : "Conso";
    }

    /**
     * Retourne le nom du filleul avec une valeur par défaut
     * selon son statut (partenaire ou consomm'acteur).
     */
    public String getLastName() {
        if (lastName != null && !lastName.isBlank()) {
            return lastName;
        }
        return partner ? "Partenaire" : "Consom'acteur";
    }
}
