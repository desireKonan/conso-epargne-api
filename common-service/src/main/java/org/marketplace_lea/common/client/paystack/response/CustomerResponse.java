package org.marketplace_lea.common.client.paystack.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Map;


@JsonIgnoreProperties(ignoreUnknown = true)
public record CustomerResponse(
        String email,
        String integration,
        String domain,
        String customer_code,
        Long id,
        String first_name,
        String last_name,
        String phone,
        Map<String, Object> metadata,
        String risk_action,
        String international_format_phone
) {
}