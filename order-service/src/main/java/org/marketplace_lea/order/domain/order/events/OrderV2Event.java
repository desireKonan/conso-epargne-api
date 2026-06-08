package org.marketplace_lea.order.domain.order.events;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Base event class for order v2 events.
 * Based on ConsoEpargneEvent from common-service.
 */
public abstract class OrderV2Event {
    private final String id;
    private final LocalDateTime timestamp;

    protected OrderV2Event() {
        this.id = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        OrderV2Event that = (OrderV2Event) object;
        return Objects.equals(id, that.id) && Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, timestamp);
    }
}
