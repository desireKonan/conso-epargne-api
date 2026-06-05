package org.marketplace_lea.common.client.wane_delivery.form;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EstimationPriceForm(
        @JsonProperty("pickup_lat")
        @NotNull(message = "La latitude de départ est obligatoire")
        @DecimalMin(value = "-90.0", message = "La latitude doit être >= -90")
        @DecimalMax(value = "90.0", message = "La latitude doit être <= 90")
        Double pickupLat,

        @JsonProperty("pickup_lng")
        @NotNull(message = "La longitude de départ est obligatoire")
        @DecimalMin(value = "-180.0", message = "La longitude doit être >= -180")
        @DecimalMax(value = "180.0", message = "La longitude doit être <= 180")
        Double pickupLng,

        @JsonProperty("drop_lat")
        @NotNull(message = "La latitude d'arrivée est obligatoire")
        @DecimalMin(value = "-90.0", message = "La latitude doit être >= -90")
        @DecimalMax(value = "90.0", message = "La latitude doit être <= 90")
        Double dropLat,

        @JsonProperty("drop_lng")
        @NotNull(message = "La longitude d'arrivée est obligatoire")
        @DecimalMin(value = "-180.0", message = "La longitude doit être >= -180")
        @DecimalMax(value = "180.0", message = "La longitude doit être <= 180")
        Double dropLng,

        @JsonProperty("vehicle_type")
        @NotBlank(message = "Le type de véhicule est obligatoire")
        String vehicleType
) {
}