package org.marketplace_lea.common.client.paystack.config;

import org.marketplace_lea.common.client.paystack.PaystackErrorDecoder;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class PaystackFeignConfig {
    @Value("${paystack.api.secret-key}")
    private String secretKey;
    
    @Bean("PaystackInterceptor")
    public RequestInterceptor authInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Authorization", "Bearer " + secretKey);
            requestTemplate.header("Content-Type", "application/json");
            requestTemplate.header("Accept", "application/json");
            requestTemplate.header("User-Agent", "Conso-Epargne-Client/1.0");
        };
    }


    @Bean("paystackErrorDecoder")
    public ErrorDecoder paystackErrorDecoder() {
        return new PaystackErrorDecoder();
    }
}