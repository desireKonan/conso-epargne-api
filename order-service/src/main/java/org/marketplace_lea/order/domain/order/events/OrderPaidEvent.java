package org.marketplace_lea.order.domain.order.events;

import lombok.Getter;
import org.marketplace_lea.common.entities.customer.CustomerV2Entity;
import org.marketplace_lea.order.common.entities.order.OrderV2Entity;

/**
 * Event published when an order is paid.
 * Based on OrderPaidEvent from common-service, adapted for v2 entities.
 */
@Getter
public class OrderPaidEvent extends OrderV2Event {
    private final OrderV2Entity order;
    private final CustomerV2Entity customer;
    private final String eventType;
    private final double latitude;
    private final double longitude;

    public OrderPaidEvent(OrderV2Entity order, CustomerV2Entity customer, String eventType, double latitude, double longitude) {
        this.order = order;
        this.customer = customer;
        this.eventType = eventType;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
