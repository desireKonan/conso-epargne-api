package org.marketplace_lea.order.domain.order.events;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Event publisher for order v2 events.
 * Based on EventPublisher from common-service, adapted for v2 events.
 */
@Component
public class OrderV2EventPublisher {
    private final ApplicationEventPublisher eventPublisher;

    public OrderV2EventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void publish(OrderV2Event event) {
        eventPublisher.publishEvent(event);
    }
}
