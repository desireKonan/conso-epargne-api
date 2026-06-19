package org.marketplace_lea.common.common.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Event publisher for order v2 events.
 * Based on EventPublisher from common-service, adapted for v2 events.
 */
@Component
public class ConsoEventPublisher<T> {
    private final ApplicationEventPublisher eventPublisher;

    public ConsoEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void publish(T event) {
        eventPublisher.publishEvent(event);
    }
}
