package org.marketplace_lea.common.client.paystack.form;

import jakarta.validation.constraints.NotBlank;

public record SubscriptionForm(
        @NotBlank
        String customer,
        @NotBlank
        String plan,
        String authorization,
        String start_date
) {

}