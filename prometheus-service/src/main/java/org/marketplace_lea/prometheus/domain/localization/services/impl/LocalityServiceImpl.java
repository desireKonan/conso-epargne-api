package org.marketplace_lea.prometheus.domain.localization.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.marketplace_lea.common.entities.LocalityEntity;
import org.marketplace_lea.common.repositories.LocalityJpaRepository;
import org.marketplace_lea.prometheus.domain.localization.dto.LocalityDto;
import org.marketplace_lea.prometheus.domain.localization.form.LocalityCreateForm;
import org.marketplace_lea.prometheus.domain.localization.form.LocalitySearchCriteria;
import org.marketplace_lea.prometheus.domain.localization.form.LocalityUpdateForm;
import org.marketplace_lea.prometheus.domain.localization.mapper.LocalityMapper;
import org.marketplace_lea.prometheus.domain.localization.services.LocalityService;
import org.marketplace_lea.prometheus.domain.localization.services.specifications.LocalitySpecifications;
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
public class LocalityServiceImpl implements LocalityService {
    private final LocalityJpaRepository repository;
    private final LocalityMapper mapper;

    @Override
    public Page<LocalityDto> search(LocalitySearchCriteria criteria, Pageable pageable) {
        Specification<LocalityEntity> spec = LocalitySpecifications.byCriteria(criteria);
        return repository.findAll(spec, pageable).map(mapper::toDto);
    }

    @Override
    public LocalityDto findById(String id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Localité non trouvée avec l'ID : " + id));
    }

    @Override
    public LocalityDto create(LocalityCreateForm form) {
        LocalityEntity entity = mapper.toEntity(form);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public LocalityDto update(String id, LocalityUpdateForm form) {
        LocalityEntity existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Localité non trouvée avec l'ID : " + id));
        mapper.updateEntity(existing, form);
        return mapper.toDto(repository.save(existing));
    }

    @Override
    public void delete(String id) {
        LocalityEntity entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Localité non trouvée avec l'ID : " + id));
        entity.setDeletedAt(LocalDateTime.now());
        repository.save(entity);
    }

    @Override
    @Transactional
    public void hardDelete(String id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Localité non trouvée avec l'ID : " + id);
        }
        repository.deleteById(id);
    }
}