package org.marketplace_lea.common.client.paystack.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PaystackResponse<T>(
        Boolean status,
        String message,
        T data
) {
}