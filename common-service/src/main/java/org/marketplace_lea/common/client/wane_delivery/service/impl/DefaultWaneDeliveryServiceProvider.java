package org.marketplace_lea.common.client.wane_delivery.service.impl;

import org.marketplace_lea.common.client.paystack.exception.PaystackException;
import org.marketplace_lea.common.client.wane_delivery.WaneDeliveryClient;
import org.marketplace_lea.common.client.wane_delivery.config.WaneDeliveryPropertiesConfig;
import org.marketplace_lea.common.client.wane_delivery.form.DeliveryRequestForm;
import org.marketplace_lea.common.client.wane_delivery.form.EstimationPriceForm;
import org.marketplace_lea.common.client.wane_delivery.form.GeoPosForm;
import org.marketplace_lea.common.client.wane_delivery.response.DeliveryRequestResponse;
import org.marketplace_lea.common.client.wane_delivery.response.EstimationPriceResponse;
import org.marketplace_lea.common.client.wane_delivery.response.WaneDeliveryResponse;
import org.marketplace_lea.common.client.wane_delivery.service.WaneDeliveryServiceProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultWaneDeliveryServiceProvider implements WaneDeliveryServiceProvider {
    private final WaneDeliveryClient waneDeliveryClient;
    private final WaneDeliveryPropertiesConfig waneDeliveryProperties;

    @Override
    public WaneDeliveryResponse<EstimationPriceResponse> estimatePrice(GeoPosForm form, String correlationId) {
        log.info("[DefaultWaneDeliveryService.estimatePrice][correlationId={}] Estimate Price for Wane API", correlationId);
        try {
            var estimationPriceForm = buildEstimationPriceForm(form);
            var estimatePriceResponse = waneDeliveryClient.estimatePrice(estimationPriceForm);
            log.info("[DefaultWaneDeliveryService.estimatePrice][correlationId={}] Message={}", correlationId, estimatePriceResponse.message());
            return estimatePriceResponse;
        } catch (PaystackException e) {
            log.error("[DefaultWaneDeliveryService.estimatePrice][correlationId={}] Error while estimating price with Wane Delivery: {}", correlationId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public WaneDeliveryResponse<DeliveryRequestResponse> createRequest(DeliveryRequestForm form, String correlationId) {
        log.info("[DefaultWaneDeliveryService.createRequest][correlationId={}] Create delivery request for Wane API", correlationId);
        try {
            form.setVehicleType(waneDeliveryProperties.getVehiculeType());
            var estimatePriceResponse = waneDeliveryClient.createDeliveryRequest(form);
            log.info("[DefaultWaneDeliveryService.estimatePrice][correlationId={}] Message={}", correlationId, estimatePriceResponse.message());
            return estimatePriceResponse;
        } catch (PaystackException e) {
            log.error("[DefaultWaneDeliveryService.estimatePrice][correlationId={}] Error while creating delivery request with Wane API: {}", correlationId, e.getMessage(), e);
            throw e;
        }
    }


    private EstimationPriceForm buildEstimationPriceForm(GeoPosForm form) {
        return new EstimationPriceForm(form.pickupLat(), form.pickupLng(), form.dropLat(), form.dropLng(), waneDeliveryProperties.getVehiculeType());
    }
}
