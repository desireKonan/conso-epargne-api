package org.marketplace_lea.prometheus.domain.parameter_config.services;

import org.marketplace_lea.prometheus.domain.parameter_config.dto.ParameterConfigDto;
import org.marketplace_lea.prometheus.domain.parameter_config.form.ParameterConfigCreateForm;
import org.marketplace_lea.prometheus.domain.parameter_config.form.ParameterConfigSearchCriteria;
import org.marketplace_lea.prometheus.domain.parameter_config.form.ParameterConfigUpdateForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ParameterConfigService {
    Page<ParameterConfigDto> search(ParameterConfigSearchCriteria criteria, Pageable pageable);
    ParameterConfigDto findById(Long id);
    ParameterConfigDto findByKey(String key);
    ParameterConfigDto create(ParameterConfigCreateForm form);
    ParameterConfigDto update(Long id, ParameterConfigUpdateForm form);
    void delete(Long id);
}