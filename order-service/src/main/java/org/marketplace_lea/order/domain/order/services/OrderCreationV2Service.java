package org.marketplace_lea.order.domain.order.services;

import org.marketplace_lea.order.domain.order.dto.OrderCreationDTO;
import org.marketplace_lea.order.domain.order.form.UpdateOrderV2Form;

public interface OrderCreationV2Service {
    OrderCreationDTO update(String id, UpdateOrderV2Form updateDTO);
}
