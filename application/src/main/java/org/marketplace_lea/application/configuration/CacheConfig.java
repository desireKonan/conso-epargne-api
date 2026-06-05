package org.marketplace_lea.application.configuration;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;



@Slf4j
@Configuration
@EnableCaching
public class CacheConfig {
    @Value(value = "${cache.config.expiration-minutes:5}")
    private int cacheExpiration;

    @Bean
    public LoadingCache<String, Integer> cacheGoogleOtpCaffeine() {
        return CacheBuilder.newBuilder()
                .expireAfterWrite(cacheExpiration, TimeUnit.MINUTES)
                .build(new CacheLoader<>() {
                    @Override
                    public Integer load(String data) {
                        return Integer.parseInt(data);
                    }
                });
    }

    @Bean
    public LoadingCache<String, String> cacheGoogleAccountCaffeine() {
        return CacheBuilder.newBuilder()
                .expireAfterWrite(cacheExpiration, TimeUnit.MINUTES)
                .build(new CacheLoader<>() {
                    @Override
                    public String load(String data) {
                        return data;
                    }
                });
    }
}
