package org.marketplace_lea.application.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString
@Component
@ConfigurationProperties(prefix = "conso.epargne")
public class CallbackWaveUrlConfig {
    private String successUrl;
    private String failedUrl;
}
