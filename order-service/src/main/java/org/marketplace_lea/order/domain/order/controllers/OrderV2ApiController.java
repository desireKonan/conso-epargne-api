package org.marketplace_lea.order.domain.order.controllers;

import org.marketplace_lea.common.common.constants.ConsoEpargneConstants;
import org.marketplace_lea.common.common.service.jwt.JwtTokenService;
import org.marketplace_lea.order.domain.order.dto.OrderCreationDTO;
import org.marketplace_lea.order.domain.order.dto.OrderValidationDTO;
import org.marketplace_lea.order.domain.order.form.CreateOrderV2Form;
import org.marketplace_lea.order.domain.order.form.OrderV2SearchForm;
import org.marketplace_lea.order.domain.order.form.OrderValidationForm;
import org.marketplace_lea.order.domain.order.handlers.OrderHandler;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderV2ApiController {
    private final OrderV2Service orderV2Service;
    private final JwtTokenService jwtTokenService;
    private final OrderHandler<CreateOrderV2Form, OrderCreationDTO> orderCreationHandler;
    private final OrderHandler<OrderValidationForm, OrderValidationDTO> orderValidationHandler;

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
    public ResponseEntity<OrderCreationDTO> create(
            @Valid @RequestBody CreateOrderV2Form createDTO,
            @RequestHeader(ConsoEpargneConstants.AUTHORIZATION) String bearerToken
    ) {
        var auth = jwtTokenService.extractCustomerInfo(bearerToken);
        createDTO.setCustomerId(auth.id());
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
    public ResponseEntity<OrderValidationDTO> changeOrderStatus(
            @Valid @RequestBody OrderValidationForm form,
            @RequestHeader(ConsoEpargneConstants.AUTHORIZATION) String bearerToken
    ) {
        var auth = jwtTokenService.extractCustomerInfo(bearerToken);
        form.setCustomerId(auth.id());
        return ResponseEntity.ok(orderValidationHandler.handle(form));
    }
}
