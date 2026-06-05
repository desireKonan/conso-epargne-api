package org.marketplace_lea.common.client.no_wallet.form;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Ignore les champs null dans le JSON
public class NoWalletForm {
    private String countryCode;

    private List<String> operatorsCode;

    private String operatorOtp;

    private String method;

    private int amount;

    private Map<String, Object> additionalInfos;

    private String callbackUrl;

    private String returnUrl;

    private String tunnel;

    private String transactionType;

    private String initiatorId;

    private String objectId;

    private String sourceId;
}
