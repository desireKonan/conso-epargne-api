package org.marketplace_lea.prometheus.domain.localization.services;

import org.marketplace_lea.prometheus.domain.localization.dto.CountryDto;
import org.marketplace_lea.prometheus.domain.localization.form.CountryCreateForm;
import org.marketplace_lea.prometheus.domain.localization.form.CountrySearchCriteria;
import org.marketplace_lea.prometheus.domain.localization.form.CountryUpdateForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CountryService {
    Page<CountryDto> search(CountrySearchCriteria criteria, Pageable pageable);
    CountryDto findById(Long id);
    CountryDto findByCode(String code);
    CountryDto create(CountryCreateForm form);
    CountryDto update(Long id, CountryUpdateForm form);
    void delete(Long id);
    CountryDto disable(Long id);      // désactivation logique
    CountryDto enable(Long id);       // réactivation
}