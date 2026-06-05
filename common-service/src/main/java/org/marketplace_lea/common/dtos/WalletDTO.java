package org.marketplace_lea.common.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WalletDTO {
    private String walletType;

    @JsonProperty("currency")
    private String devise;

    private float balance;

    private float reservAmount;
}
