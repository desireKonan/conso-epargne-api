package org.marketplace_lea.common.client.wave.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class WaveCheckout {
    @JsonProperty("id")
    private String id;

    @JsonProperty("transaction_id")
    private String transactionId;

    private String amount;

    private String currency;

    @JsonProperty("checkout_status")
    private String checkoutStatus;

    @JsonProperty("payment_status")
    private String paymentStatus;

    @JsonProperty("client_reference")
    private String clientReference;

    @JsonProperty("business_name")
    private String businessName;

    @JsonProperty("aggregated_merchant_id")
    private String aggregatedMerchantId;

    @JsonProperty("last_payment_error")
    private Object lastPaymentError;

    @JsonProperty("wave_launch_url")
    private String waveLaunchUrl;

    @JsonProperty("error_url")
    private String errorUrl;

    @JsonProperty("success_url")
    private String successUrl;

    @JsonProperty("when_created")
    private String whenCreated;

    @JsonProperty("when_completed")
    private String whenCompleted;

    @JsonProperty("when_expires")
    private String whenExpires;

    private boolean hasNullRef() {
        return this.id == null;
    }
}