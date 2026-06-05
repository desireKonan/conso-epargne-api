package org.marketplace_lea.common.client.wane_delivery.service;

import org.marketplace_lea.common.client.wane_delivery.form.DeliveryRequestForm;
import org.marketplace_lea.common.client.wane_delivery.response.DeliveryRequestResponse;
import org.marketplace_lea.common.client.wane_delivery.response.EstimationPriceResponse;
import org.marketplace_lea.common.client.wane_delivery.response.WaneDeliveryResponse;
import org.marketplace_lea.common.client.wane_delivery.form.GeoPosForm;

public interface WaneDeliveryService {
    WaneDeliveryResponse<EstimationPriceResponse> estimatePrice(GeoPosForm form, String correlationId);

    WaneDeliveryResponse<DeliveryRequestResponse> createRequest(DeliveryRequestForm form, String correlationId);
}
