package org.marketplace_lea.prometheus.domain.currency.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.marketplace_lea.common.entities.CurrencyV2Entity;
import org.marketplace_lea.common.repositories.CurrencyJpaRepository;
import org.marketplace_lea.prometheus.domain.currency.dto.CurrencyDto;
import org.marketplace_lea.prometheus.domain.currency.form.CurrencyCreateForm;
import org.marketplace_lea.prometheus.domain.currency.form.CurrencySearchCriteria;
import org.marketplace_lea.prometheus.domain.currency.form.CurrencyUpdateForm;
import org.marketplace_lea.prometheus.domain.currency.mapper.CurrencyMapper;
import org.marketplace_lea.prometheus.domain.currency.services.CurrencyService;
import org.marketplace_lea.prometheus.domain.currency.services.specifications.CurrencySpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyJpaRepository repository;
    private final CurrencyMapper mapper;

    @Override
    public Page<CurrencyDto> search(CurrencySearchCriteria criteria, Pageable pageable) {
        Specification<CurrencyV2Entity> spec = CurrencySpecifications.byCriteria(criteria);
        return repository.findAll(spec, pageable)
                .map(mapper::toDto);
    }

    @Override
    public CurrencyDto findByCode(String code) {
        return repository.findById(code)
                .map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Devise non trouvée avec le code : " + code));
    }

    @Override
    public CurrencyDto create(CurrencyCreateForm form) {
        if (repository.existsById(form.getCode())) {
            throw new IllegalArgumentException("Une devise avec le code '" + form.getCode() + "' existe déjà.");
        }
        CurrencyV2Entity entity = mapper.toEntity(form);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public CurrencyDto update(String code, CurrencyUpdateForm form) {
        CurrencyV2Entity existing = repository.findById(code)
                .orElseThrow(() -> new EntityNotFoundException("Devise non trouvée avec le code : " + code));

        if (!existing.getCode().equals(form.getCode()) && repository.existsById(form.getCode())) {
            throw new IllegalArgumentException("Le code '" + form.getCode() + "' est déjà utilisé.");
        }

        mapper.updateEntity(existing, form);
        return mapper.toDto(repository.save(existing));
    }

    @Override
    @Transactional
    public void delete(String code) {
        if (!repository.existsById(code)) {
            throw new EntityNotFoundException("Devise non trouvée avec le code : " + code);
        }
        repository.deleteById(code);
    }
}