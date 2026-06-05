package org.marketplace_lea.common.client.yellika.sms.config;

import feign.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class YellikaSmsConfig {
    @Bean
    public YellikaSmsInterceptor requestInterceptor(@Value("${yellika.sms.token}") String yellikaSmsToken) {
        return new YellikaSmsInterceptor(yellikaSmsToken);
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
