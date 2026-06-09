package org.marketplace_lea.prometheus.domain.payment_method.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.marketplace_lea.prometheus.domain.payment_method.dto.PaymentMethodDTO;
import org.marketplace_lea.prometheus.domain.payment_method.form.PaymentMethodCreateForm;
import org.marketplace_lea.prometheus.domain.payment_method.form.PaymentMethodSearchCriteria;
import org.marketplace_lea.prometheus.domain.payment_method.form.PaymentMethodUpdateForm;
import org.marketplace_lea.prometheus.domain.payment_method.services.PaymentMethodService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment-methods")
public class PaymentMethodController {
    private final PaymentMethodService paymentMethodService;

    @GetMapping
    public ResponseEntity<Page<PaymentMethodDTO>> search(
            @ModelAttribute PaymentMethodSearchCriteria criteria,
            @PageableDefault(size = 20, sort = "label", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(paymentMethodService.search(criteria, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentMethodDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentMethodService.findById(id));
    }

    @GetMapping("/provider/{provider}")
    public ResponseEntity<PaymentMethodDTO> getByProvider(@PathVariable String provider) {
        return ResponseEntity.ok(paymentMethodService.findByProvider(provider));
    }

    @GetMapping("/mobile")
    public ResponseEntity<List<PaymentMethodDTO>> getMobilePaymentMethods() {
        return ResponseEntity.ok(paymentMethodService.findAllMobilePaymentMethods());
    }

    @PostMapping
    public ResponseEntity<PaymentMethodDTO> create(@Valid @ModelAttribute PaymentMethodCreateForm form) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentMethodService.create(form));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentMethodDTO> update(
            @PathVariable Long id,
            @Valid @ModelAttribute PaymentMethodUpdateForm form
    ) {
        return ResponseEntity.ok(paymentMethodService.update(id, form));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        paymentMethodService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/availability")
    public ResponseEntity<PaymentMethodDTO> changeAvailability(
            @PathVariable Long id,
            @RequestParam boolean available) {
        return ResponseEntity.ok(paymentMethodService.changeAvailability(id, available));
    }
}