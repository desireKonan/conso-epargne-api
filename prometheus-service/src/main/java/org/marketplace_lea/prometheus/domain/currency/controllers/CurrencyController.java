package org.marketplace_lea.prometheus.domain.currency.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.marketplace_lea.prometheus.domain.currency.dto.CurrencyDto;
import org.marketplace_lea.prometheus.domain.currency.form.CurrencyCreateForm;
import org.marketplace_lea.prometheus.domain.currency.form.CurrencySearchCriteria;
import org.marketplace_lea.prometheus.domain.currency.form.CurrencyUpdateForm;
import org.marketplace_lea.prometheus.domain.currency.services.CurrencyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/currencies")
public class CurrencyController {
    private final CurrencyService currencyService;

    @GetMapping
    public ResponseEntity<Page<CurrencyDto>> search(
            @ModelAttribute CurrencySearchCriteria criteria,
            @PageableDefault(size = 20, sort = "code", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(currencyService.search(criteria, pageable));
    }

    @GetMapping("/{code}")
    public ResponseEntity<CurrencyDto> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(currencyService.findByCode(code));
    }

    @PostMapping
    public ResponseEntity<CurrencyDto> create(@Valid @RequestBody CurrencyCreateForm form) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(currencyService.create(form));
    }

    @PutMapping("/{code}")
    public ResponseEntity<CurrencyDto> update(@PathVariable String code, @Valid @RequestBody CurrencyUpdateForm form) {
        return ResponseEntity.ok(currencyService.update(code, form));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Void> delete(@PathVariable String code) {
        currencyService.delete(code);
        return ResponseEntity.noContent().build();
    }
}