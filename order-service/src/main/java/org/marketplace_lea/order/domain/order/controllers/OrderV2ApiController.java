package org.marketplace_lea.order.domain.order.controllers;

import org.marketplace_lea.order.domain.order.dto.CreateOrderV2Form;
import org.marketplace_lea.order.domain.order.dto.OrderV2DTO;
import org.marketplace_lea.order.domain.order.dto.OrderV2SearchForm;
import org.marketplace_lea.order.domain.order.dto.UpdateOrderV2Form;
import org.marketplace_lea.order.domain.order.services.OrderCreationHandler;
import org.marketplace_lea.order.domain.order.services.OrderCreationV2Service;
import org.marketplace_lea.order.domain.order.services.OrderV2Service;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderV2ApiController {
    private final OrderV2Service orderV2Service;
    private final OrderCreationHandler orderCreationHandler;
    private final OrderCreationV2Service orderCreationV2Service;

    @GetMapping
    public ResponseEntity<Page<OrderV2DTO>> getAll(
            OrderV2SearchForm filterCriteria,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(orderV2Service.getAll(filterCriteria, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderV2DTO> getById(@PathVariable String id) {
        Optional<OrderV2DTO> order = orderV2Service.findById(id);
        return order.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<OrderV2DTO> create(@Valid @RequestBody CreateOrderV2Form createDTO) {
        OrderV2DTO created = orderCreationHandler.handleOrderCreation(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderV2DTO> update(
            @PathVariable String id,
            @Valid @RequestBody UpdateOrderV2Form updateDTO) {
        OrderV2DTO updated = orderCreationV2Service.update(id, updateDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        orderV2Service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> changeOrderStatus(
            @PathVariable String id,
            @RequestParam String status) {
        orderV2Service.changeOrderStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/validate")
    public ResponseEntity<Void> validateOrder(@PathVariable String id) {
        orderV2Service.validateOrder(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable String id) {
        orderV2Service.cancelOrder(id);
        return ResponseEntity.ok().build();
    }
}
