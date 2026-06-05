package org.marketplace_lea.prometheus.domain.localization.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.marketplace_lea.prometheus.domain.localization.dto.CountryDto;
import org.marketplace_lea.prometheus.domain.localization.form.CountryCreateForm;
import org.marketplace_lea.prometheus.domain.localization.form.CountrySearchCriteria;
import org.marketplace_lea.prometheus.domain.localization.form.CountryUpdateForm;
import org.marketplace_lea.prometheus.domain.localization.services.CountryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/countries")
@RequiredArgsConstructor
public class CountryController {
    private final CountryService service;

    @GetMapping
    public ResponseEntity<Page<CountryDto>> search(
            @ModelAttribute CountrySearchCriteria criteria,
            @PageableDefault(size = 20, sort = "label", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(service.search(criteria, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CountryDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<CountryDto> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(service.findByCode(code));
    }

    @PostMapping
    public ResponseEntity<CountryDto> create(@Valid @RequestBody CountryCreateForm form) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(form));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CountryDto> update(@PathVariable Long id, @Valid @RequestBody CountryUpdateForm form) {
        return ResponseEntity.ok(service.update(id, form));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/disable")
    public ResponseEntity<CountryDto> disable(@PathVariable Long id) {
        return ResponseEntity.ok(service.disable(id));
    }

    @PatchMapping("/{id}/enable")
    public ResponseEntity<CountryDto> enable(@PathVariable Long id) {
        return ResponseEntity.ok(service.enable(id));
    }
}