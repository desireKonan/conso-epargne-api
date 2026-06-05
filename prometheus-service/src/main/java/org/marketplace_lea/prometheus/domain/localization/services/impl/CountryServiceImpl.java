package org.marketplace_lea.prometheus.domain.localization.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.marketplace_lea.common.entities.CountryV2Entity;
import org.marketplace_lea.common.repositories.CountryJpaRepository;
import org.marketplace_lea.prometheus.domain.localization.dto.CountryDto;
import org.marketplace_lea.prometheus.domain.localization.form.CountryCreateForm;
import org.marketplace_lea.prometheus.domain.localization.form.CountrySearchCriteria;
import org.marketplace_lea.prometheus.domain.localization.form.CountryUpdateForm;
import org.marketplace_lea.prometheus.domain.localization.mapper.CountryMapper;
import org.marketplace_lea.prometheus.domain.localization.services.CountryService;
import org.marketplace_lea.prometheus.domain.localization.services.specifications.CountrySpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {
    private final CountryJpaRepository repository;
    private final CountryMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<CountryDto> search(CountrySearchCriteria criteria, Pageable pageable) {
        Specification<CountryV2Entity> spec = CountrySpecifications.byCriteria(criteria);
        return repository.findAll(spec, pageable)
                .map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public CountryDto findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Pays non trouvé avec l'ID : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public CountryDto findByCode(String code) {
        return repository.getByCode(code)
                .map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Pays non trouvé avec le code : " + code));
    }

    @Override
    @Transactional
    public CountryDto create(CountryCreateForm form) {
        // Vérifier l'unicité du code et de l'indicatif
        if (repository.getByCode(form.getCode()).isPresent()) {
            throw new IllegalArgumentException("Un pays avec le code '" + form.getCode() + "' existe déjà.");
        }
        CountryV2Entity entity = mapper.toEntity(form);
        entity.setEnabled(true);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    @Transactional
    public CountryDto update(Long id, CountryUpdateForm form) {
        CountryV2Entity existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pays non trouvé avec l'ID : " + id));

        // Si le code change, vérifier l'unicité
        if (!existing.getCode().equals(form.getCode()) && repository.getByCode(form.getCode()).isPresent()) {
            throw new IllegalArgumentException("Le code '" + form.getCode() + "' est déjà utilisé.");
        }

        mapper.updateEntity(existing, form);
        return mapper.toDto(repository.save(existing));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Pays non trouvé avec l'ID : " + id);
        }
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public CountryDto disable(Long id) {
        CountryV2Entity entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pays non trouvé avec l'ID : " + id));
        entity.setEnabled(false);
        entity.setDisabledAt(LocalDateTime.now());
        return mapper.toDto(repository.save(entity));
    }

    @Override
    @Transactional
    public CountryDto enable(Long id) {
        CountryV2Entity entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pays non trouvé avec l'ID : " + id));
        entity.setEnabled(true);
        entity.setDisabledAt(null);
        return mapper.toDto(repository.save(entity));
    }
}