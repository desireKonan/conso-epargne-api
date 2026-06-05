package org.marketplace_lea.prometheus.domain.localization.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.marketplace_lea.common.entities.DistrictEntity;
import org.marketplace_lea.common.entities.LocalityEntity;
import org.marketplace_lea.common.repositories.DistrictJpaRepository;
import org.marketplace_lea.common.repositories.LocalityJpaRepository;
import org.marketplace_lea.prometheus.domain.localization.dto.DistrictDto;
import org.marketplace_lea.prometheus.domain.localization.form.DistrictCreateForm;
import org.marketplace_lea.prometheus.domain.localization.form.DistrictSearchCriteria;
import org.marketplace_lea.prometheus.domain.localization.form.DistrictUpdateForm;
import org.marketplace_lea.prometheus.domain.localization.mapper.DistrictMapper;
import org.marketplace_lea.prometheus.domain.localization.services.DistrictService;
import org.marketplace_lea.prometheus.domain.localization.services.specifications.DistrictSpecifications;
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
public class DistrictServiceImpl implements DistrictService {
    private final DistrictJpaRepository districtRepository;
    private final LocalityJpaRepository localityRepository;
    private final DistrictMapper mapper;

    @Override
    public Page<DistrictDto> search(DistrictSearchCriteria criteria, Pageable pageable) {
        Specification<DistrictEntity> spec = DistrictSpecifications.byCriteria(criteria);
        return districtRepository.findAll(spec, pageable)
                .map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public DistrictDto findById(String id) {
        return districtRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("District non trouvé avec l'ID : " + id));
    }

    @Override
    @Transactional
    public DistrictDto create(DistrictCreateForm form) {
        // Vérifier que la localité existe
        LocalityEntity locality = localityRepository.findById(form.getLocalityId())
                .orElseThrow(() -> new IllegalArgumentException("Localité introuvable avec l'ID : " + form.getLocalityId()));

        DistrictEntity entity = mapper.toEntity(form);
        entity.setLocality(locality);
        // L'ID sera généré automatiquement par @PrePersist
        return mapper.toDto(districtRepository.save(entity));
    }

    @Override
    @Transactional
    public DistrictDto update(String id, DistrictUpdateForm form) {
        DistrictEntity existing = districtRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("District non trouvé avec l'ID : " + id));

        LocalityEntity locality = localityRepository.findById(form.getLocalityId())
                .orElseThrow(() -> new IllegalArgumentException("Localité introuvable avec l'ID : " + form.getLocalityId()));

        mapper.updateEntity(existing, form);
        existing.setLocality(locality);
        return mapper.toDto(districtRepository.save(existing));
    }

    @Override
    @Transactional
    public void softDelete(String id) {
        DistrictEntity entity = districtRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("District non trouvé avec l'ID : " + id));
        entity.setDeletedAt(LocalDateTime.now());
        districtRepository.save(entity);
    }

    @Override
    @Transactional
    public void hardDelete(String id) {
        if (!districtRepository.existsById(id)) {
            throw new EntityNotFoundException("District non trouvé avec l'ID : " + id);
        }
        districtRepository.deleteById(id);
    }
}