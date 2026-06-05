package org.marketplace_lea.prometheus.domain.currency.services;

import org.marketplace_lea.prometheus.domain.currency.dto.CurrencyDto;
import org.marketplace_lea.prometheus.domain.currency.form.CurrencyCreateForm;
import org.marketplace_lea.prometheus.domain.currency.form.CurrencySearchCriteria;
import org.marketplace_lea.prometheus.domain.currency.form.CurrencyUpdateForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CurrencyService {
    Page<CurrencyDto> search(CurrencySearchCriteria criteria, Pageable pageable);
    CurrencyDto findByCode(String code);
    CurrencyDto create(CurrencyCreateForm form);
    CurrencyDto update(String code, CurrencyUpdateForm form);
    void delete(String code);
}