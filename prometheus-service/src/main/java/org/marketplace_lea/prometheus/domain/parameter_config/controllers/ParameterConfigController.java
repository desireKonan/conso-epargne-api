package org.marketplace_lea.prometheus.domain.parameter_config.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.marketplace_lea.prometheus.domain.parameter_config.dto.ParameterConfigDto;
import org.marketplace_lea.prometheus.domain.parameter_config.form.ParameterConfigCreateForm;
import org.marketplace_lea.prometheus.domain.parameter_config.form.ParameterConfigSearchCriteria;
import org.marketplace_lea.prometheus.domain.parameter_config.form.ParameterConfigUpdateForm;
import org.marketplace_lea.prometheus.domain.parameter_config.services.ParameterConfigService;
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
@RequestMapping("/api/parameters")
public class ParameterConfigController {
    private final ParameterConfigService parameterConfigService;

    @GetMapping
    public ResponseEntity<Page<ParameterConfigDto>> search(
            @ModelAttribute ParameterConfigSearchCriteria criteria,
            @PageableDefault(size = 20, sort = "key", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(parameterConfigService.search(criteria, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParameterConfigDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(parameterConfigService.findById(id));
    }

    @GetMapping("/key/{key}")
    public ResponseEntity<ParameterConfigDto> getByKey(@PathVariable String key) {
        return ResponseEntity.ok(parameterConfigService.findByKey(key));
    }

    @PostMapping
    public ResponseEntity<ParameterConfigDto> create(@Valid @RequestBody ParameterConfigCreateForm form) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(parameterConfigService.create(form));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParameterConfigDto> update(
            @PathVariable Long id,
            @Valid @RequestBody ParameterConfigUpdateForm form
    ) {
        return ResponseEntity.ok(parameterConfigService.update(id, form));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        parameterConfigService.delete(id);
        return ResponseEntity.noContent().build();
    }
}