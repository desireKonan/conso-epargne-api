package org.marketplace_lea.prometheus.domain.parameter_config.services;

import org.marketplace_lea.prometheus.domain.parameter_config.dto.ParameterConfigDto;
import org.marketplace_lea.prometheus.domain.parameter_config.form.ParameterConfigCreateForm;
import org.marketplace_lea.prometheus.domain.parameter_config.form.ParameterConfigSearchCriteria;
import org.marketplace_lea.prometheus.domain.parameter_config.form.ParameterConfigUpdateForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ParameterConfigService {
    Page<ParameterConfigDto> search(ParameterConfigSearchCriteria criteria, Pageable pageable);

    ParameterConfigDto findById(Long id);

    ParameterConfigDto findByKey(String key);

    Optional<String> getStringValue(String key);

    Optional<Boolean> getBooleanValue(String key);

    Optional<Integer> getIntValue(String key);

    Optional<Double> getDoubleValue(String key);

    Optional<Float> getFloatValue(String key);

    String getStringValueOrDefault(String key, String defaultValue);

    boolean getBooleanValueOrDefault(String key, boolean defaultValue);

    int getIntValueOrDefault(String key, int defaultValue);

    double getDoubleValueOrDefault(String key, double defaultValue);

    float getFloatValueOrDefault(String key, float defaultValue);

    ParameterConfigDto create(ParameterConfigCreateForm form);

    ParameterConfigDto update(Long id, ParameterConfigUpdateForm form);

    void delete(Long id);
}