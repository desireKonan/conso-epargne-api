package org.marketplace_lea.common.client.wane_delivery.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DeliveryRequestResponse(
        String id,

        @JsonProperty("company_key")
        String companyKey,

        @JsonProperty("request_number")
        String requestNumber,

        @JsonProperty("is_paid")
        int isPaid,

        @JsonProperty("payment_opt")
        int paymentOpt,

        @JsonProperty("vehicle_type_name")
        String typeName
) {
}