package org.marketplace_lea.common.client.wane_delivery.config;

import org.marketplace_lea.common.client.wane_delivery.WaneDeliveryErrorDecoder;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WaneDeliveryFeignConfig {
    private final WaneDeliveryPropertiesConfig waneProperties;

    @Bean("WaneDeliveryInterceptor")
    public RequestInterceptor authInterceptor() {
        return new WaneDeliveryClientInterceptor(waneProperties);
    }

    @Bean("waneDeliveryErrorDecoder")
    public ErrorDecoder waneDeliveryErrorDecoder() {
        return new WaneDeliveryErrorDecoder();
    }
}
