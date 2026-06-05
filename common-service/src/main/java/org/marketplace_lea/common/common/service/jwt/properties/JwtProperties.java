package org.marketplace_lea.common.common.service.jwt.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
     private String secret;
     private int expirationMs;
}
