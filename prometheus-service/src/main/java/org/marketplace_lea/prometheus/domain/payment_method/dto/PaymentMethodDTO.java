package org.marketplace_lea.prometheus.domain.payment_method.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodDTO {
    private long id;

    private String label;

    private boolean available;

    private boolean online;

    private String provider;

    private String image;

    private float fees;
}
