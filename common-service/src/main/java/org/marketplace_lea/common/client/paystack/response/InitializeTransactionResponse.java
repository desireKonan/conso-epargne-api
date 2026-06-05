package org.marketplace_lea.common.client.paystack.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record InitializeTransactionResponse(
        @JsonProperty(value = "authorization_url")
        String authorizationUrl,

        @JsonProperty(value = "access_code")
        String accessCode,

        String reference
) {

}