package org.marketplace_lea.order.domain.order.dto;

import org.marketplace_lea.order.common.entities.order.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderV2DTO(
    String id,
    String customerId,
    String customerName,
    String voucherId,
    String address,
    BigDecimal latitude,
    BigDecimal longitude,
    float totalAmount,
    float fees,
    float onlineFees,
    float deliveryFees,
    boolean delivered,
    OrderStatus status,
    boolean hasOnlinePayment,
    String provider,
    LocalDateTime createdAt,
    LocalDateTime validatedAt,
    LocalDateTime canceledAt,
    LocalDateTime paidAt,
    LocalDateTime deliveredAt
) {}
