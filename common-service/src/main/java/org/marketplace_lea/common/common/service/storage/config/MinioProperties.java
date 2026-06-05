package org.marketplace_lea.common.common.service.storage.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {
    private String baseUrl;
    private int port;
    private String accessKey;
    private String secretKey;
    private String bucketName;
    private boolean secure = false;
}
