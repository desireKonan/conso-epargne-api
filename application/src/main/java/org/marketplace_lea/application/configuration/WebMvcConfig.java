package org.marketplace_lea.application.configuration;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.marketplace_lea.common.common.constants.Router;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/templates/**")
                .addResourceLocations("/resources/")
                .setCacheControl(
                        CacheControl
                        .maxAge(30L, TimeUnit.DAYS)
                        .cachePublic()
                )
                .resourceChain(true);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(Router.API_ROOT_V1 + "/**")
                .allowedOrigins("*");
    }

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
