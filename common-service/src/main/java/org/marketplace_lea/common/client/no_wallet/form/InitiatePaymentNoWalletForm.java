package org.marketplace_lea.common.client.no_wallet.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InitiatePaymentNoWalletForm {
    @JsonProperty("transaction_id")
    private String transactionId;
    
    @JsonProperty("country_code")
    private String countryCode;
    
    @JsonProperty("operators_code")
    private List<String> operatorsCode;
    
    @JsonProperty("operator_otp")
    private String operatorOtp;
    
    private String method;
    private int amount;
    
    @JsonProperty("additional_infos")
    private Map<String, Object> additionalInfos;
    
    @JsonProperty("callback_url")
    private String callbackUrl;
    
    @JsonProperty("return_url")
    private String returnUrl;
    
    private String tunnel;
}