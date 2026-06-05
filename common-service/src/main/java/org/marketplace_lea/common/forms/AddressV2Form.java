package org.marketplace_lea.common.forms;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record AddressV2Form(
        @NotBlank(message = "Veuillez entrer votre addresse !")
        String label,

        @Max(value = 90)
        @Min(value = -90)
        float latitude,

        @Max(value = 180)
        @Min(value = -180)
        float longitude
) {
    public BigDecimal getLatitude() {
        return BigDecimal.valueOf(latitude);
    }

    public BigDecimal getLongitude() {
        return BigDecimal.valueOf(longitude);
    }
}
