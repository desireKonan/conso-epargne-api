package org.marketplace_lea.prometheus.domain.localization.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.marketplace_lea.prometheus.domain.localization.dto.DistrictDto;
import org.marketplace_lea.prometheus.domain.localization.form.DistrictCreateForm;
import org.marketplace_lea.prometheus.domain.localization.form.DistrictSearchCriteria;
import org.marketplace_lea.prometheus.domain.localization.form.DistrictUpdateForm;
import org.marketplace_lea.prometheus.domain.localization.services.DistrictService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/districts")
public class DistrictController {
    private final DistrictService districtService;

    @GetMapping
    public ResponseEntity<Page<DistrictDto>> search(
            @ModelAttribute DistrictSearchCriteria criteria,
            @PageableDefault(size = 20, sort = "label", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(districtService.search(criteria, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DistrictDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(districtService.findById(id));
    }

    @PostMapping
    public ResponseEntity<DistrictDto> create(@Valid @RequestBody DistrictCreateForm form) {
        return ResponseEntity.status(HttpStatus.CREATED).body(districtService.create(form));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DistrictDto> update(@PathVariable String id, @Valid @RequestBody DistrictUpdateForm form) {
        return ResponseEntity.ok(districtService.update(id, form));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDelete(@PathVariable String id) {
        districtService.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<Void> hardDelete(@PathVariable String id) {
        districtService.hardDelete(id);
        return ResponseEntity.noContent().build();
    }
}