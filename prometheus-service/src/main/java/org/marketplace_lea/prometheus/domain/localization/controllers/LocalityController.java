package org.marketplace_lea.prometheus.domain.localization.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.marketplace_lea.prometheus.domain.localization.dto.LocalityDto;
import org.marketplace_lea.prometheus.domain.localization.form.LocalityCreateForm;
import org.marketplace_lea.prometheus.domain.localization.form.LocalitySearchCriteria;
import org.marketplace_lea.prometheus.domain.localization.form.LocalityUpdateForm;
import org.marketplace_lea.prometheus.domain.localization.services.LocalityService;
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
@RequestMapping("/api/localities")

public class LocalityController {
    private final LocalityService localityService;

    @GetMapping
    public ResponseEntity<Page<LocalityDto>> search(
            @ModelAttribute LocalitySearchCriteria criteria,
            @PageableDefault(size = 20, sort = "label", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(localityService.search(criteria, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocalityDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(localityService.findById(id));
    }

    @PostMapping
    public ResponseEntity<LocalityDto> create(@Valid @RequestBody LocalityCreateForm form) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(localityService.create(form));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocalityDto> update(@PathVariable String id, @Valid @RequestBody LocalityUpdateForm form) {
        return ResponseEntity.ok(localityService.update(id, form));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        localityService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<Void> hardDelete(@PathVariable String id) {
        localityService.hardDelete(id);
        return ResponseEntity.noContent().build();
    }
}