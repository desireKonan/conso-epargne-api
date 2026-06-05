package org.marketplace_lea.common.client.wave.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;

public class WaveClientConfig {
    //    @Bean
    //    public WaveRequestInterceptor requestInterceptor(@Value("${wave.api.key}") String waveAPiKey) {
    //        return new WaveRequestInterceptor(waveAPiKey);
    //    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
