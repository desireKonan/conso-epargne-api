package org.marketplace_lea.common.client.wave.dto;

import java.time.Instant;

public record WavePayoutResponse(
        String id,
        String currency,
        String receiveAmount,
        String fee,
        String mobile,
        String name,
        String status,
        Instant timestamp
) {
}
