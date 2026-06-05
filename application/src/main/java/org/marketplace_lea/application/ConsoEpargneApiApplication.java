package org.marketplace_lea.application;

import org.marketplace_lea.common.common.service.storage.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(scanBasePackages = {
		"org.marketplace_lea.common",
		"org.marketplace_lea.prometheus",
		"org.marketplace_lea.application"
})
@EntityScan(basePackages = {
		"org.marketplace_lea.common",
		"org.marketplace_lea.prometheus",
		"org.marketplace_lea.application"
})
@EnableFeignClients(basePackages = {
		"org.marketplace_lea.common",
		"org.marketplace_lea.prometheus",
		"org.marketplace_lea.application"
})
@EnableJpaRepositories(basePackages = {
		"org.marketplace_lea.common",
		"org.marketplace_lea.prometheus",
		"org.marketplace_lea.application"
})
@EnableConfigurationProperties(StorageProperties.class)
public class ConsoEpargneApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsoEpargneApiApplication.class, args);
	}

}
