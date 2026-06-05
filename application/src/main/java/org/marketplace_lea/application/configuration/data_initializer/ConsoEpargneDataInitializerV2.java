package org.marketplace_lea.application.configuration.data_initializer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.marketplace_lea.application.configuration.data_initializer.dto.CountryConfig;
import org.marketplace_lea.application.configuration.data_initializer.dto.DistrictConfig;
import org.marketplace_lea.application.configuration.data_initializer.dto.InitDataConfig;
import org.marketplace_lea.application.configuration.data_initializer.dto.LocalityConfig;
import org.marketplace_lea.application.configuration.data_initializer.dto.ParameterConfigs;
import org.marketplace_lea.application.configuration.data_initializer.dto.TransactionTemplateMessageConfig;
import org.marketplace_lea.application.configuration.data_initializer.properties.DataInitializerProperties;
import org.marketplace_lea.application.configuration.data_initializer.service.AccountTypeInitializer;
import org.marketplace_lea.application.configuration.data_initializer.service.CountryInitializer;
import org.marketplace_lea.application.configuration.data_initializer.service.CurrencyInitializer;
import org.marketplace_lea.application.configuration.data_initializer.service.DistrictInitializer;
import org.marketplace_lea.application.configuration.data_initializer.service.LocalityInitializer;
import org.marketplace_lea.application.configuration.data_initializer.service.NotificationTemplateInitializer;
import org.marketplace_lea.application.configuration.data_initializer.service.ParameterInitializer;
import org.marketplace_lea.common.common.service.storage.StorageService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class ConsoEpargneDataInitializerV2 implements ApplicationListener<ApplicationReadyEvent> {
    private final AccountTypeInitializer accountTypeInitializer;
    private final CurrencyInitializer currencyInitializer;
    private final ParameterInitializer parameterInitializer;
    private final CountryInitializer countryInitializer;
    private final LocalityInitializer localityInitializer;
    private final DistrictInitializer districtInitializer;
    private final NotificationTemplateInitializer notificationTemplateInitializer;

    private final StorageService storageService;
    private final ObjectMapper objectMapper;

    private final DataInitializerProperties dataInitializerProperties;

    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        if (!dataInitializerProperties.isEnabled()) {
            log.info("Initialisation des données désactivée via configuration.");
            return;
        }

        try {
            /// Initialisation de la configuration
            storageService.init();

            var urls = dataInitializerProperties.getUrls();

            /// Config (Data).
            InitDataConfig config = objectMapper.readValue(urls.get("data").getInputStream(), InitDataConfig.class);
            accountTypeInitializer.initialize(config.getAccountTypes(), dataInitializerProperties.isResetBeforeInit());

            /// Currencies.
            currencyInitializer.initializeCurrencies(config.getCurrencies(), dataInitializerProperties.isResetBeforeInit());

            /// Parameters.
            List<ParameterConfigs> parameterConfigs = objectMapper.readValue(urls.get("parameters").getInputStream(), new TypeReference<>() {
            });
            parameterInitializer.initializeParameters(parameterConfigs, dataInitializerProperties.isResetBeforeInit());


            /// Countries.
            List<CountryConfig> countriesConfigs = objectMapper.readValue(urls.get("countries").getInputStream(), new TypeReference<>() {
            });
            countryInitializer.initializeCountries(countriesConfigs, dataInitializerProperties.isResetBeforeInit());


            /// Localities.
            List<LocalityConfig> localityConfigs = objectMapper.readValue(urls.get("localities").getInputStream(), new TypeReference<>() {
            });
            localityInitializer.initializeLocalities(localityConfigs, dataInitializerProperties.isResetBeforeInit());


            /// Districts.
            List<DistrictConfig> districtConfigs = objectMapper.readValue(urls.get("districts").getInputStream(), new TypeReference<>() {
            });
            districtInitializer.initializeDistricts(districtConfigs, dataInitializerProperties.isResetBeforeInit());


            /// Templates Notifications.
            List<TransactionTemplateMessageConfig> notificationConfigs = objectMapper.readValue(urls.get("templates").getInputStream(), new TypeReference<>() {
            });
            notificationTemplateInitializer.initializeTemplates(notificationConfigs, dataInitializerProperties.isResetBeforeInit());
            log.info("Initialisation terminée avec succès.");
        } catch (IOException e) {
            log.error("Impossible de lire le fichier de configuration JSON : {}", e.getMessage(), e);
            throw new RuntimeException("Erreur critique au démarrage : lecture JSON échouée", e);
        }
    }
}
