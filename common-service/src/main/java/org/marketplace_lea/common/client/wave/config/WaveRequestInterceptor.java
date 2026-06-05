package org.marketplace_lea.common.client.wave.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static org.marketplace_lea.common.common.utils.SecurityUtils.AUTHORIZATION_HEADER_KEY;
import static org.marketplace_lea.common.common.utils.SecurityUtils.AUTHORIZATION_TOKEN_PREFIX;
import static org.marketplace_lea.common.common.constants.ConsoEpargneConstants.PAYMENT_API_CONTENT_TYPE;

@Slf4j
@AllArgsConstructor
public class WaveRequestInterceptor implements RequestInterceptor {
    private String accessToken;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("Content-Type", PAYMENT_API_CONTENT_TYPE);
        requestTemplate.header(AUTHORIZATION_HEADER_KEY, String.format("%s %s", AUTHORIZATION_TOKEN_PREFIX, accessToken));
    }
}
