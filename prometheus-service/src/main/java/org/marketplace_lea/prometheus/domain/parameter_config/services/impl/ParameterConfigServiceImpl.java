package org.marketplace_lea.prometheus.domain.parameter_config.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jcajce.provider.keystore.util.ParameterUtil;
import org.marketplace_lea.common.common.utils.TypeUtils;
import org.marketplace_lea.common.entities.parameters.ParameterConfigEntity;
import org.marketplace_lea.common.repositories.ParameterConfigJpaRepository;
import org.marketplace_lea.prometheus.domain.parameter_config.dto.ParameterConfigDto;
import org.marketplace_lea.prometheus.domain.parameter_config.form.ParameterConfigCreateForm;
import org.marketplace_lea.prometheus.domain.parameter_config.form.ParameterConfigSearchCriteria;
import org.marketplace_lea.prometheus.domain.parameter_config.form.ParameterConfigUpdateForm;
import org.marketplace_lea.prometheus.domain.parameter_config.mapper.ParameterConfigMapper;
import org.marketplace_lea.prometheus.domain.parameter_config.services.ParameterConfigService;
import org.marketplace_lea.prometheus.domain.parameter_config.services.specifications.ParameterConfigSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParameterConfigServiceImpl implements ParameterConfigService {
    private final ParameterConfigJpaRepository repository;
    private final ParameterConfigMapper mapper;

    @Override
    public Page<ParameterConfigDto> search(ParameterConfigSearchCriteria criteria, Pageable pageable) {
        Specification<ParameterConfigEntity> spec = ParameterConfigSpecifications.byCriteria(criteria);
        return repository.findAll(spec, pageable)
                .map(mapper::toDto);
    }

    @Override
    public ParameterConfigDto findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Paramètre introuvable avec l'ID : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public ParameterConfigDto findByKey(String key) {
        Specification<ParameterConfigEntity> spec = (root, q, cb) -> cb.equal(root.get("key"), key);
        return repository.findOne(spec)
                .map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Paramètre introuvable avec la clé : " + key));
    }

    @Override
    public Optional<String> getStringValue(String key) {
        return repository.getByKey(key)
                .map(TypeUtils::stringValue);
    }

    @Override
    public Optional<Boolean> getBooleanValue(String key) {
        return repository.getByKey(key)
                .map(TypeUtils::booleanValue);
    }

    @Override
    public Optional<Integer> getIntValue(String key) {
        return repository.getByKey(key)
                .map(TypeUtils::intValue);
    }

    @Override
    public Optional<Double> getDoubleValue(String key) {
        return repository.getByKey(key)
                .map(TypeUtils::doubleValue);
    }

    @Override
    public Optional<Float> getFloatValue(String key) {
        return repository.getByKey(key)
                .map(TypeUtils::floatValue);
    }

    @Override
    public String getStringValueOrDefault(String key, String defaultValue) {
        return getStringValue(key).orElse(defaultValue);
    }

    @Override
    public boolean getBooleanValueOrDefault(String key, boolean defaultValue) {
        return getBooleanValue(key).orElse(defaultValue);
    }

    @Override
    public int getIntValueOrDefault(String key, int defaultValue) {
        return getIntValue(key).orElse(defaultValue);
    }

    @Override
    public double getDoubleValueOrDefault(String key, double defaultValue) {
        return getDoubleValue(key).orElse(defaultValue);
    }

    @Override
    public float getFloatValueOrDefault(String key, float defaultValue) {
        return getFloatValue(key).orElse(defaultValue);
    }

    @Override
    public ParameterConfigDto create(ParameterConfigCreateForm form) {
        if (repository.existsByKey(form.getKey())) {
            throw new IllegalArgumentException("Un paramètre avec la clé '" + form.getKey() + "' existe déjà.");
        }
        ParameterConfigEntity entity = mapper.toEntity(form);
        entity.setDataType(form.getDataType().name()); // conversion enum → String
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public ParameterConfigDto update(Long id, ParameterConfigUpdateForm form) {
        ParameterConfigEntity existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Paramètre introuvable avec l'ID : " + id));

        if (!existing.getKey().equals(form.getKey()) && repository.existsByKey(form.getKey())) {
            throw new IllegalArgumentException("La clé '" + form.getKey() + "' est déjà utilisée.");
        }

        mapper.updateEntity(existing, form);
        if (form.getDataType() != null) {
            existing.setDataType(form.getDataType().name());
        }
        return mapper.toDto(repository.save(existing));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Paramètre introuvable avec l'ID : " + id);
        }
        repository.deleteById(id);
    }
}