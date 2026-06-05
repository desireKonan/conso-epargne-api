package org.marketplace_lea.common.client.wane_delivery.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EstimationPriceResponse(
        double distance,
        int time,
        @JsonProperty(namespace = "base_price")
        double basePrice,
        @JsonProperty(namespace = "price_per_distance")
        double pricePerDistance,
        @JsonProperty(namespace = "price_per_time")
        double pricePerTime,
        double total,
        String currency
) {
}
