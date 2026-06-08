package org.marketplace_lea.order.domain.order.dto;

import org.marketplace_lea.order.common.entities.order.OrderStatus;

import java.math.BigDecimal;

public record UpdateOrderV2Form(
    String voucherId,
    String address,
    BigDecimal latitude,
    BigDecimal longitude,
    Float totalAmount,
    Float fees,
    Float onlineFees,
    Float deliveryFees,
    Boolean delivered,
    OrderStatus status,
    Boolean hasOnlinePayment,
    String provider
) {}
