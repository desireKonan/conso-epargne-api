package org.marketplace_lea.common.forms;

import jakarta.validation.constraints.NotBlank;

public record UpdateCustomerForm(
        @NotBlank(message = "Le nom doit être définit !")
        String firstName,

        @NotBlank(message = "Le prénoms doit être définit !")
        String lastName,

        @NotBlank(message = "L'email doit être définit !")
        String email
) {

}
