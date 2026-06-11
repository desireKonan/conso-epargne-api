package org.marketplace_lea.order.domain.order.controllers;

import org.marketplace_lea.order.domain.order.dto.OrderCreationDTO;
import org.marketplace_lea.order.domain.order.form.CreateOrderV2Form;
import org.marketplace_lea.order.domain.order.form.OrderV2SearchForm;
import org.marketplace_lea.order.domain.order.form.OrderValidationForm;
import org.marketplace_lea.order.domain.order.services.OrderHandler;
import org.marketplace_lea.order.domain.order.services.OrderV2Service;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderV2ApiController {
    private final OrderV2Service orderV2Service;
    private final OrderHandler<CreateOrderV2Form, OrderCreationDTO> orderCreationHandler;

    @GetMapping
    public ResponseEntity<Page<OrderCreationDTO>> getAll(
            @ModelAttribute OrderV2SearchForm filterCriteria,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(orderV2Service.getAll(filterCriteria, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderCreationDTO> getById(@PathVariable String id) {
        Optional<OrderCreationDTO> order = orderV2Service.findById(id);
        return order.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<OrderCreationDTO> create(@Valid @RequestBody CreateOrderV2Form createDTO) {
        OrderCreationDTO created = orderCreationHandler.handle(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        orderV2Service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/validate")
    public ResponseEntity<Void> changeOrderStatus(@Valid @RequestBody OrderValidationForm form) {
        return ResponseEntity.ok().build();
    }
}
