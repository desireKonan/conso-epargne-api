package org.marketplace_lea.common.client.no_wallet.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NoWalletCallbackForm {
    private String status;

    @JsonProperty("transaction_id")
    private String transactionId;

    @JsonProperty("additional_infos")
    private Map<String, String> additionalInfos;

    private int amount;
    private String currency;

    @JsonProperty("fee_percent")
    private int feePercent;

    @JsonProperty("fee_value")
    private int feeValue;

    private int balance;

    @JsonProperty("balance_before")
    private int balanceBefore;

    @JsonProperty("balance_after")
    private int balanceAfter;

    @JsonProperty("transaction_method")
    private String transactionMethod;

    @JsonProperty("transaction_phone_number")
    private String transactionPhoneNumber;

    @JsonProperty("transaction_dialcode")
    private String transactionDialcode;

    private String signature;

    @JsonProperty("transaction_country_code")
    private String transactionCountryCode;

    @JsonProperty("transaction_service_name")
    private String transactionServiceName;

    @JsonProperty("transaction_observation")
    private String transactionObservation;
}

