package org.marketplace_lea.common.client.yellika.sms.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record YellikaResponse(
        boolean success,
        String message,
        Object data
) {
}