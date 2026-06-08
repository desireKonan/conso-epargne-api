package org.marketplace_lea.order.domain.inventory.controllers;

import org.marketplace_lea.order.domain.inventory.dto.CreateEnsignV2Form;
import org.marketplace_lea.order.domain.inventory.dto.EnsignV2DTO;
import org.marketplace_lea.order.domain.inventory.dto.EnsignV2SearchForm;
import org.marketplace_lea.order.domain.inventory.dto.UpdateEnsignV2Form;
import org.marketplace_lea.order.domain.inventory.services.EnsignV2Service;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ensigns")
public class EnsignV2ApiController {
    private final EnsignV2Service ensignV2Service;

    @GetMapping
    public ResponseEntity<Page<EnsignV2DTO>> findAll(
            EnsignV2SearchForm form,
            @PageableDefault(sort = "rank", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(ensignV2Service.findAll(form, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnsignV2DTO> getById(@PathVariable String id) {
        Optional<EnsignV2DTO> ensign = ensignV2Service.findById(id);
        return ensign.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EnsignV2DTO> createEnsign(
            @Valid @RequestPart("data") CreateEnsignV2Form form,
            @RequestPart("image") org.springframework.web.multipart.MultipartFile image) {
        EnsignV2DTO created = ensignV2Service.createEnsign(form, image);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnsignV2DTO> updateEnsign(
            @PathVariable String id,
            @Valid @RequestPart("data") UpdateEnsignV2Form form,
            @RequestPart(value = "image", required = false) org.springframework.web.multipart.MultipartFile image) {
        EnsignV2DTO updated = ensignV2Service.updateEnsign(id, form, image);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/rank")
    public ResponseEntity<Void> updateRank(
            @PathVariable String id,
            @RequestParam(required = false) String referralEnsignId,
            @RequestParam(required = false) Integer position) {
        if (position != null) {
            ensignV2Service.updateRank(id, position);
        } else if (referralEnsignId != null) {
            ensignV2Service.updateRank(id, referralEnsignId);
        }
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/action")
    public ResponseEntity<Void> performAction(
            @PathVariable String id,
            @RequestParam String action,
            @RequestParam(required = false) String referralEnsignId) {
        ensignV2Service.performAction(id, action, referralEnsignId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnsign(@PathVariable String id) {
        ensignV2Service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
