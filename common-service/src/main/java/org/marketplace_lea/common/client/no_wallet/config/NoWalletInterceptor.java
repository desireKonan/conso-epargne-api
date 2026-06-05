package org.marketplace_lea.common.client.no_wallet.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class NoWalletInterceptor implements RequestInterceptor {
    private final NoWalletApiPropertiesConfig properties;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        log.info("[NoWalletInterceptor.authInterceptor] Properties No Wallet {}", properties);
        requestTemplate.header("Content-Type", "application/json");
        requestTemplate.header("Accept", "application/json");
    }
}
