package org.marketplace_lea.common.client.wave.form;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WavePayoutForm(
        String currency,
        @JsonProperty("receive_amount")
        String receiveAmount,
        String name,
        String mobile
) {
}
