package org.marketplace_lea.order.domain.order.services.impl;

import org.marketplace_lea.common.common.exceptions.ConsoEpargneException;
import org.marketplace_lea.common.common.exceptions.ConsoEpargneNotFoundDataException;
import org.marketplace_lea.common.common.utils.Calculator;
import org.marketplace_lea.common.common.utils.GeneratorUtils;
import org.marketplace_lea.common.entities.DistrictEntity;
import org.marketplace_lea.common.entities.customer.CustomerV2Entity;
import org.marketplace_lea.common.repositories.customer.CustomerV2JpaRepository;
import org.marketplace_lea.order.common.entities.inventory.ProductV2Entity;
import org.marketplace_lea.order.common.entities.order.CartItemV2Entity;
import org.marketplace_lea.order.common.entities.order.OrderStatus;
import org.marketplace_lea.order.common.entities.order.OrderV2Entity;
import org.marketplace_lea.order.common.repository.order.CartItemV2JpaRepository;
import org.marketplace_lea.order.common.repository.order.OrderV2JpaRepository;
import org.marketplace_lea.order.common.repository.order.VoucherV2JpaRepository;
import org.marketplace_lea.order.domain.order.dto.CreateOrderV2Form;
import org.marketplace_lea.order.domain.order.dto.OrderV2DTO;
import org.marketplace_lea.order.domain.order.events.OrderPaidEvent;
import org.marketplace_lea.order.domain.order.events.OrderV2EventPublisher;
import org.marketplace_lea.order.domain.order.mapper.OrderV2Mapper;
import org.marketplace_lea.order.domain.order.services.OrderCreationHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.marketplace_lea.prometheus.domain.payment_method.repository.PaymentMethodJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    private final CartItemV2JpaRepository cartItemV2JpaRepository;
    private final VoucherV2JpaRepository voucherV2JpaRepository;
    private final PaymentMethodJpaRepository paymentMethodJpaRepository;
    private final OrderV2EventPublisher eventPublisher;

    @Override
    public OrderV2DTO handleOrderCreation(CreateOrderV2Form createDTO) {
        // Step 1: Get customer
        CustomerV2Entity customer = customerRepository.findById(createDTO.customerId())
                .orElseThrow(() -> new ConsoEpargneNotFoundDataException("Client introuvable: " + createDTO.customerId()));

        // Step 2: Create Order (Transaction).
        var orderCreated = createOrder(createDTO, customer);

        // Step 3: Publish delivery event if payment method is "CUSTOMER" (optional).
        if ("CUSTOMER".equals(orderCreated.getProvider())) {
            var deliveryEvent = new OrderPaidEvent(
                    orderCreated,
                    customer,
                    "DELIVERY",
                    createDTO.determineLatitude(),
                    createDTO.determineLongitude()
            );
            eventPublisher.publish(deliveryEvent);
            log.info("[DefaultOrderCreationHandler.handleOrderCreation] Delivery event published");
        } else {
            log.info("[DefaultOrderCreationHandler.handleOrderCreation] Event publishing skipped (no CUSTOMER payment method)");
        }

        // Step 4: Return Element in parse data.
        return orderV2Mapper.toDTO(orderCreated);
    }


    /// Privates Helpers.
    private void validateCartOrder(String customerId) {
        // Step 2: Validate cart is not empty
        // TODO: Implement cart validation when CustomerV2Service is available
        List<CartItemV2Entity> carts = cartItemV2JpaRepository.getAllByCustomerId(customerId);
        if (carts.isEmpty()) {
            throw new ConsoEpargneNotFoundDataException("Le chariot de commande est vide ! Veuillez ajouter des elements avant de faire une commande !");
        }
        log.info("[DefaultOrderCreationHandler.handleOrderCreation] Cart validation skipped (TODO)");

        // Step 3: Validate no sold out products in cart
        // TODO: Implement sold out product validation when CustomerV2Service is available
        List<String> cartItemsWithSoldoutProduct = carts.stream()
                .filter(cartItemV2Entity -> cartItemV2Entity.getProduct().isSoldOut())
                .map(CartItemV2Entity::getProduct)
                .map(ProductV2Entity::getLabel)
                .toList();
        if (!cartItemsWithSoldoutProduct.isEmpty()) {
            String productNames = String.join(",", cartItemsWithSoldoutProduct);
            throw new ConsoEpargneException("Les produits " + productNames + " sont en rupture de stocks !", HttpStatus.BAD_REQUEST);
        }
        log.info("[DefaultOrderCreationHandler.handleOrderCreation] Sold out product validation skipped (TODO)");
    }


    @Transactional
    private OrderV2Entity createOrder(CreateOrderV2Form createDTO, CustomerV2Entity customer) {
        log.info("[DefaultOrderCreationHandler.createOrder] Starting order creation for customer: {}", createDTO.customerId());

        // Step 2: Validation de la commande.
        validateCartOrder(customer.getId());

        // Step 3: Get payment method
        // TODO: Implement payment method handling when PaymentMethodService is available
        log.info("[DefaultOrderCreationHandler.createOrder] Payment method handling skipped (TODO)");

        // Step 5: Build order
        OrderV2Entity order = orderV2Mapper.toEntity(createDTO);
        order.setId(GeneratorUtils.generateOrderId());
        order.setCustomer(customer);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        // Step 6: Handle voucher if provided
        if (createDTO.voucherId() != null && !createDTO.voucherId().isBlank()) {
            log.info("[DefaultOrderCreationHandler.createOrder] Voucher handled in PaymentDetailService");
            var voucherFound = voucherV2JpaRepository.getValidVoucherByCustomerId(customer.getId());
            voucherFound.ifPresent(order::setVoucher);
        }

        // Step 7: Handle fees (with district).
        float deliveryFees = Optional.ofNullable(customer.getDistrict())
                .map(DistrictEntity::getFees)
                .orElse(0.0f);

        order.setDeliveryFees(deliveryFees);

        // Step 8: Calculate amount with fees
        float amount = Calculator.calculateAmountWithFees(order.retrieveTotalOrderPrice(), deliveryFees);
        order.setTotalAmount((float) (amount + order.retrieveTotalOrderPrice()));

        var paymentDetailsFound = paymentMethodJpaRepository.findByProvider(createDTO.provider());

        paymentDetailsFound.ifPresent(paymentMethodEntity -> {
            if (paymentMethodEntity.isMobileMoneyPayment()) {
                order.setOnlineFees(amount);
            }
        });

        // Step 8: Save order
        OrderV2Entity orderSaved = orderRepository.save(order);
        log.info("[DefaultOrderCreationHandler.createOrder] Order saved with ID: {}", orderSaved.getId());

        // Step 10: Delete customer cart
        // TODO: Implement cart deletion when CustomerV2Service is available
        cartItemV2JpaRepository.deleteByCustomerId(customer.getId());
        log.info("[DefaultOrderCreationHandler.createOrder] Cart deletion skipped (TODO)");

        log.info("[DefaultOrderCreationHandler.createOrder] Order creation completed for customer: {}", createDTO.customerId());
        return orderSaved;
    }
}
