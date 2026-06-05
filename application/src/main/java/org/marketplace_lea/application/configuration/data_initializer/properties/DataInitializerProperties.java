package org.marketplace_lea.application.configuration.data_initializer.properties;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "app.data-init")
public class DataInitializerProperties {
    private Map<String, @NotBlank Resource> urls = new HashMap<>();

    private boolean enabled = true;

    private boolean resetBeforeInit = false;
}
