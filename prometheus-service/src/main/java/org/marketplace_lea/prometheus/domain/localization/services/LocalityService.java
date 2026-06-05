package org.marketplace_lea.prometheus.domain.localization.services;

import org.marketplace_lea.prometheus.domain.localization.dto.LocalityDto;
import org.marketplace_lea.prometheus.domain.localization.form.LocalityCreateForm;
import org.marketplace_lea.prometheus.domain.localization.form.LocalitySearchCriteria;
import org.marketplace_lea.prometheus.domain.localization.form.LocalityUpdateForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LocalityService {
    Page<LocalityDto> search(LocalitySearchCriteria criteria, Pageable pageable);
    LocalityDto findById(String id);
    LocalityDto create(LocalityCreateForm form);
    LocalityDto update(String id, LocalityUpdateForm form);
    void delete(String id);           // suppression logique
    void hardDelete(String id);       // suppression physique
}