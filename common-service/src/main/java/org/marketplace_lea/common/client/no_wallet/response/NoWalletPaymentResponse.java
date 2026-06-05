package org.marketplace_lea.common.client.no_wallet.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record NoWalletPaymentResponse(
    String country,
    String currency,
    String signature,
    @JsonProperty("available_operator")
    List<String> availableOperator,
    @JsonProperty("authorized_operator")
    List<String> authorizedOperator,
    @JsonProperty("payment_url_operator")
    String paymentUrlOperator,
    @JsonProperty("status_payment")
    String statusPayment,
    String message,
    @JsonProperty("observation_error")
    String observationError
) {}