package org.marketplace_lea.prometheus.domain.template_messages.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.marketplace_lea.prometheus.domain.template_messages.dto.TransactionNotificationTemplateDto;
import org.marketplace_lea.prometheus.domain.template_messages.form.NotificationTemplateSearchCriteria;
import org.marketplace_lea.prometheus.domain.template_messages.form.TransactionNotificationTemplateForm;
import org.marketplace_lea.prometheus.domain.template_messages.services.TransactionNotificationTemplateService;
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
@RequestMapping("/notification-templates")
public class TransactionNotificationTemplateController {
    private final TransactionNotificationTemplateService templateService;

    @GetMapping
    public ResponseEntity<Page<TransactionNotificationTemplateDto>> search(
            @ModelAttribute NotificationTemplateSearchCriteria criteria,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(templateService.search(criteria, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionNotificationTemplateDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(templateService.findById(id));
    }

    @PostMapping
    public ResponseEntity<TransactionNotificationTemplateDto> create(
            @Valid @RequestBody TransactionNotificationTemplateForm form
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(templateService.create(form));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionNotificationTemplateDto> update(
            @PathVariable Long id,
            @Valid @RequestBody TransactionNotificationTemplateForm form
    ) {
        return ResponseEntity.ok(templateService.update(id, form));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        templateService.delete(id);
        return ResponseEntity.noContent().build();
    }
}