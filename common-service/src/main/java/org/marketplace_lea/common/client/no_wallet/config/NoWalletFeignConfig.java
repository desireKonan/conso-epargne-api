package org.marketplace_lea.common.client.no_wallet.config;

import org.marketplace_lea.common.client.no_wallet.NoWalletErrorDecoder;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NoWalletFeignConfig {
    private final NoWalletApiPropertiesConfig properties;

    @Bean("NoWalletInterceptor")
    public RequestInterceptor noWalletAuthInterceptor() {
        return new NoWalletInterceptor(properties);
    }

    @Bean("NoWalletErrorDecoder")
    public ErrorDecoder noWalletErrorDecoder() {
        return new NoWalletErrorDecoder();
    }
}
