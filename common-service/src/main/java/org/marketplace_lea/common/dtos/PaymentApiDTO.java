package org.marketplace_lea.common.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentApiDTO {
    private double amount;
    private String msisdn;
    private String otp;
    private Long idPaymentMethod;
}
