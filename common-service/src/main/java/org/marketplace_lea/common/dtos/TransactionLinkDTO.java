package org.marketplace_lea.common.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * A DTO for the {@link District} entity
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionLinkDTO implements Serializable {
    private String transactionStatusUrl;
    private String cancelTransactionUrl;
    private String gatewayUrl;
    private String message;

    public TransactionLinkDTO(String transactionStatusUrl, String cancelTransactionUrl, String gatewayUrl) {
        this.transactionStatusUrl = transactionStatusUrl;
        this.cancelTransactionUrl = cancelTransactionUrl;
        this.gatewayUrl = gatewayUrl;
    }
}