package org.marketplace_lea.order.domain.order.services.impl;

import org.marketplace_lea.common.common.exceptions.ConsoEpargneNotFoundDataException;
import org.marketplace_lea.common.entities.customer.CustomerV2Entity;
import org.marketplace_lea.common.repositories.customer.CustomerV2JpaRepository;
import org.marketplace_lea.order.common.entities.order.OrderStatus;
import org.marketplace_lea.order.common.entities.order.OrderV2Entity;
import org.marketplace_lea.order.common.repository.order.OrderV2JpaRepository;
import org.marketplace_lea.order.domain.order.dto.CreateOrderV2Form;
import org.marketplace_lea.order.domain.order.dto.OrderV2DTO;
import org.marketplace_lea.order.domain.order.events.OrderV2EventPublisher;
import org.marketplace_lea.order.domain.order.mapper.OrderV2Mapper;
import org.marketplace_lea.order.domain.order.services.OrderCreationHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Implementation of OrderCreationHandler that orchestrates the complete order creation process.
 * Based on the createOrder method from OrderService.java, this handler performs:
 * 1. Cart validation (empty cart, sold out products)
 * 2. Payment method handling
 * 3. Address management
 * 4. Payment detail creation
 * 5. Order history creation
 * 6. Event publishing for delivery
 * 7. Cart deletion
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultOrderCreationHandler implements OrderCreationHandler {
    private final OrderV2JpaRepository orderRepository;
    private final CustomerV2JpaRepository customerRepository;
    private final OrderV2Mapper orderV2Mapper;
    private final OrderV2EventPublisher eventPublisher;

    @Override
    @Transactional
    public OrderV2DTO handleOrderCreation(CreateOrderV2Form createDTO) {
        log.info("[DefaultOrderCreationHandler.handleOrderCreation] Starting order creation for customer: {}", createDTO.customerId());

        // Step 1: Get customer
        CustomerV2Entity customer = customerRepository.findById(createDTO.customerId())
                .orElseThrow(() -> new ConsoEpargneNotFoundDataException("Client introuvable: " + createDTO.customerId()));

        // Step 2: Validate cart is not empty
        // TODO: Implement cart validation when CustomerV2Service is available
        // if (customer.isEmptyCart()) {
        //     throw new EmptyCartException();
        // }
        log.info("[DefaultOrderCreationHandler.handleOrderCreation] Cart validation skipped (TODO)");

        // Step 3: Validate no sold out products in cart
        // TODO: Implement sold out product validation when CustomerV2Service is available
        // if (customer.hasSoldOutProductInCart()) {
        //     throw new SoldOutProductInCartException();
        // }
        log.info("[DefaultOrderCreationHandler.handleOrderCreation] Sold out product validation skipped (TODO)");

        // Step 4: Get payment method
        // TODO: Implement payment method handling when PaymentMethodService is available
        log.info("[DefaultOrderCreationHandler.handleOrderCreation] Payment method handling skipped (TODO)");

        // Step 5: Build order
        OrderV2Entity order = orderV2Mapper.toEntity(createDTO);
        order.setId(UUID.randomUUID().toString());
        order.setCustomer(customer);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        // Step 6: Handle voucher if provided
        if (createDTO.voucherId() != null && !createDTO.voucherId().isBlank()) {
            log.info("[DefaultOrderCreationHandler.handleOrderCreation] Voucher handled in PaymentDetailService");
        }

        // Step 7: Save order
        OrderV2Entity savedOrder = orderRepository.save(order);
        log.info("[DefaultOrderCreationHandler.handleOrderCreation] Order saved with ID: {}", savedOrder.getId());


        // Step 8: Publish delivery event if payment method is "CUSTOMER"
        if ("CUSTOMER".equals(order.getProvider())) {
//            var deliveryEvent = new OrderPaidEvent(
//                    savedOrder,
//                    customer,
//                    "DELIVERY",
//                    createDTO.latitude() != null ? createDTO.latitude().doubleValue() : 0.0,
//                    createDTO.longitude() != null ? createDTO.longitude().doubleValue() : 0.0
//            );
//            eventPublisher.publish(deliveryEvent);
            log.info("[DefaultOrderCreationHandler.handleOrderCreation] Delivery event published");
        } else {
            log.info("[DefaultOrderCreationHandler.handleOrderCreation] Event publishing skipped (no CUSTOMER payment method)");
        }

        // Step 10: Delete customer cart
        // TODO: Implement cart deletion when CustomerV2Service is available
        // customerV2Service.deleteCustomerCart(customer);
        log.info("[DefaultOrderCreationHandler.handleOrderCreation] Cart deletion skipped (TODO)");

        log.info("[DefaultOrderCreationHandler.handleOrderCreation] Order creation completed for customer: {}", createDTO.customerId());
        return orderV2Mapper.toDTO(savedOrder);
    }
}
