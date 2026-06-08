package org.marketplace_lea.order.domain.order.dto;


import org.marketplace_lea.order.common.entities.order.OrderStatus;

public record OrderV2SearchForm(
    String customerId,
    OrderStatus status,
    Boolean delivered,
    Boolean hasOnlinePayment,
    String provider
) {}
