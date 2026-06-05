package org.marketplace_lea.common.forms;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.math.BigDecimal;
import java.util.List;

public record CreateCustomerForm(
        String id,
        @NotBlank(message = "Renseigner votre login !") String login,
        String parentCode,
        @NotBlank(message = "Renseigner votre nom !") String lastname,
        @NotBlank(message = "Renseigner votre prénom !") String firstname,
        @NotBlank(message = "Renseigner votre email !") String email,
        @NotEmpty(message = "Renseigner vos numéros !") List<String> phoneNumbers,
        String address,
        BigDecimal longitude,
        BigDecimal latitude,
        String accountId
) {
}
