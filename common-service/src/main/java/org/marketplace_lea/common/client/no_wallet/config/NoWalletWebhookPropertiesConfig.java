package org.marketplace_lea.common.client.no_wallet.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString
@Component
@ConfigurationProperties(prefix = "nowallet.webhook")
public class NoWalletWebhookPropertiesConfig {
    private String secretKey;
    private String uniqueKey;
}
