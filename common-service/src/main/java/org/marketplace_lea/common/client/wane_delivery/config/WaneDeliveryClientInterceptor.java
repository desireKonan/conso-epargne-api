package org.marketplace_lea.common.client.wane_delivery.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class WaneDeliveryClientInterceptor implements RequestInterceptor {
    private final WaneDeliveryPropertiesConfig waneProperties;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        log.info("[WaneDeliveryFeignConfig.authInterceptor] Properties wane {}", waneProperties);
        requestTemplate.header("Content-Type", "application/json");
        requestTemplate.header("Accept", "application/json");
        requestTemplate.header("X-API-KEY", waneProperties.getToken());
    }
}
