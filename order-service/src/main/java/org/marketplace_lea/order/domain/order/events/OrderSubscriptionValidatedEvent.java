package org.marketplace_lea.order.domain.order.events;

import lombok.Getter;
import org.marketplace_lea.order.common.entities.order.OrderItemV2Entity;

/**
 * Event published when an order is paid.
 * Based on OrderPaidEvent from common-service, adapted for v2 entities.
 */
@Getter
public final class OrderSubscriptionValidatedEvent extends OrderV2Event {
    private final OrderItemV2Entity orderItem;
    private final String customerId;

    public OrderSubscriptionValidatedEvent(OrderItemV2Entity orderItem, String customerId) {
        this.orderItem = orderItem;
        this.customerId = customerId;
    }
}
