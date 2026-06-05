package org.marketplace_lea.common.client.wane_delivery;

import org.marketplace_lea.common.client.wane_delivery.config.WaneDeliveryFeignConfig;
import org.marketplace_lea.common.client.wane_delivery.response.DeliveryRequestResponse;
import org.marketplace_lea.common.client.wane_delivery.response.EstimationPriceResponse;
import org.marketplace_lea.common.client.wane_delivery.response.WaneDeliveryResponse;
import org.marketplace_lea.common.client.wane_delivery.form.DeliveryRequestForm;
import org.marketplace_lea.common.client.wane_delivery.form.EstimationPriceForm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "waneDeliveryClient",
        url = "${wane.delivery.api.url}/${wane.delivery.api.partner-id}",
        configuration = WaneDeliveryFeignConfig.class
)
public interface WaneDeliveryClient {
    // Delivery API
    @PostMapping("/estimate-price")
    WaneDeliveryResponse<EstimationPriceResponse> estimatePrice(@RequestBody EstimationPriceForm form);

    @PostMapping("/create-request")
    WaneDeliveryResponse<DeliveryRequestResponse> createDeliveryRequest(@RequestBody DeliveryRequestForm form);
}
