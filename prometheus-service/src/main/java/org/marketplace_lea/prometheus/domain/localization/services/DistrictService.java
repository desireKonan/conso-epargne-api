package org.marketplace_lea.prometheus.domain.localization.services;

import org.marketplace_lea.prometheus.domain.localization.dto.DistrictDto;
import org.marketplace_lea.prometheus.domain.localization.form.DistrictCreateForm;
import org.marketplace_lea.prometheus.domain.localization.form.DistrictSearchCriteria;
import org.marketplace_lea.prometheus.domain.localization.form.DistrictUpdateForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DistrictService {
    Page<DistrictDto> search(DistrictSearchCriteria criteria, Pageable pageable);
    DistrictDto findById(String id);
    DistrictDto create(DistrictCreateForm form);
    DistrictDto update(String id, DistrictUpdateForm form);
    void softDelete(String id);   // suppression logique
    void hardDelete(String id);   // suppression physique
}