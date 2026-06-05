package org.marketplace_lea.common.forms;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record AddressForm(
        long id,
        @NotBlank(message = "Veuillez entrer votre addresse !")
        String label,
        String districtId,
        String latitude,
        String longitude
) {

    public BigDecimal getLatitude() {
        return BigDecimal.valueOf(Float.parseFloat(latitude));
    }
}
