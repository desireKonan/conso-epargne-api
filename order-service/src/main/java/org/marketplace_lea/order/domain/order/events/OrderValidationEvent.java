package org.marketplace_lea.order.domain.order.events;

import lombok.Getter;
import org.marketplace_lea.order.common.entities.order.OrderV2Entity;

@Getter
public final class OrderValidationEvent extends OrderV2Event {
    private final OrderV2Entity order;

    public OrderValidationEvent(OrderV2Entity order) {
        this.order = order;
    }
}
