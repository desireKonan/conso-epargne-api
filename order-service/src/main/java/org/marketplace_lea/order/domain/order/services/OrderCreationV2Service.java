package org.marketplace_lea.order.domain.order.services;

import org.marketplace_lea.order.domain.order.dto.OrderV2DTO;
import org.marketplace_lea.order.domain.order.dto.UpdateOrderV2Form;

public interface OrderCreationV2Service {
    OrderV2DTO update(String id, UpdateOrderV2Form updateDTO);
}
