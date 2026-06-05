package org.marketplace_lea.common.client.wane_delivery.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString
@Component
@ConfigurationProperties(prefix = "wane.delivery.api")
public final class WaneDeliveryPropertiesConfig {
    private String url;

    private String partnerId;

    private String vehiculeType;

    private String token;
}
