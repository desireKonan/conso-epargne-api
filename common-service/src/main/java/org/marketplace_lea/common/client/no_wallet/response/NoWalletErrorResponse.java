package org.marketplace_lea.common.client.no_wallet.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record NoWalletErrorResponse(
        @JsonProperty("status_code")
        int statusCode,
        String message,
        @JsonProperty("status_payment")
        String statusPayment
) {
}
