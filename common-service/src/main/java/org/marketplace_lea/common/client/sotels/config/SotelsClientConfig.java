package org.marketplace_lea.common.client.sotels.config;

import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

import static org.marketplace_lea.common.common.constants.ConsoEpargneConstants.PAYMENT_API_CONTENT_TYPE;
import static org.marketplace_lea.common.common.constants.ConsoEpargneConstants.PAYMENT_API_HEADER_PROD_ENV;
import static org.marketplace_lea.common.common.constants.ConsoEpargneConstants.PAYMENT_API_HEADER_X_API_KEY;
import static org.marketplace_lea.common.common.constants.ConsoEpargneConstants.PAYMENT_API_HEADER_X_MERCHANT_ID;

public class SotelsClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("x-apikey", PAYMENT_API_HEADER_X_API_KEY);
            requestTemplate.header("x-merchantId", PAYMENT_API_HEADER_X_MERCHANT_ID);
            requestTemplate.header("environnment", PAYMENT_API_HEADER_PROD_ENV);
            requestTemplate.header("Content-Type", PAYMENT_API_CONTENT_TYPE);
        };
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
