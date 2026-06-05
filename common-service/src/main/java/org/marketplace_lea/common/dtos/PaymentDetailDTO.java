package org.marketplace_lea.common.dtos;

public record PaymentDetailDTO(
        Float amount,
        Float fees,
        String reference,
        String status,
        Long idPaymentMethod
) {
}
