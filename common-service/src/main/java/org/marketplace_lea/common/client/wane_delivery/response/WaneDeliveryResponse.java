package org.marketplace_lea.common.client.wane_delivery.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record WaneDeliveryResponse<T>(
        String message,
        T data
) {
}
