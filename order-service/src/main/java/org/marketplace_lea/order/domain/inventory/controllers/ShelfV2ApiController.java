package org.marketplace_lea.order.domain.inventory.controllers;

import org.marketplace_lea.order.domain.inventory.dto.CreateShelfV2Form;
import org.marketplace_lea.order.domain.inventory.dto.ShelfV2DTO;
import org.marketplace_lea.order.domain.inventory.dto.UpdateShelfV2Form;
import org.marketplace_lea.order.domain.inventory.mapper.ShelfV2Mapper;
import org.marketplace_lea.order.domain.inventory.services.ShelfV2Service;
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

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shelves")
public class ShelfV2ApiController {

    private final ShelfV2Service shelfV2Service;
    private final ShelfV2Mapper shelfV2Mapper;

    @GetMapping
    public ResponseEntity<Page<ShelfV2DTO>> searchShelves(
            @RequestParam(required = false) String ensignId,
            @PageableDefault(sort = "rank", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(shelfV2Service.searchShelves(ensignId, pageable));
    }

    @GetMapping("/by-ensign/{ensignId}")
    public ResponseEntity<List<ShelfV2DTO>> findShelvesByEnsignId(@PathVariable String ensignId) {
        List<ShelfV2DTO> shelves = shelfV2Service.findShelvesByEnsignId(ensignId);
        return ResponseEntity.ok(shelves);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShelfV2DTO> getById(@PathVariable String id) {
        Optional<ShelfV2DTO> shelf = shelfV2Service.findById(id);
        return shelf.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ShelfV2DTO> createShelf(
            @Valid @RequestPart("data") CreateShelfV2Form form,
            @RequestPart("image") org.springframework.web.multipart.MultipartFile image) {
        ShelfV2DTO created = shelfV2Service.createShelf(form, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShelfV2DTO> updateShelf(
            @PathVariable String id,
            @Valid @RequestPart("data") UpdateShelfV2Form form,
            @RequestPart(value = "image", required = false) org.springframework.web.multipart.MultipartFile image) {
        ShelfV2DTO updated = shelfV2Service.updateShelf(id, form, image);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/rank")
    public ResponseEntity<Void> updateRank(
            @PathVariable String id,
            @RequestParam(required = false) String referralShelfId,
            @RequestParam(required = false) Integer position) {
        if (position != null) {
            shelfV2Service.updateRank(id, position);
        } else if (referralShelfId != null) {
            shelfV2Service.updateRank(id, referralShelfId);
        }
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/action")
    public ResponseEntity<Void> performAction(
            @PathVariable String id,
            @RequestParam String action,
            @RequestParam(required = false) String referralShelfId) {
        shelfV2Service.performAction(id, action, referralShelfId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShelf(@PathVariable String id) {
        shelfV2Service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
