package org.marketplace_lea.order.domain.order.handlers.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.marketplace_lea.common.common.exceptions.ConsoEpargneNotFoundDataException;
import org.marketplace_lea.common.entities.customer.CustomerV2Entity;
import org.marketplace_lea.common.repositories.customer.CustomerV2JpaRepository;
import org.marketplace_lea.order.common.entities.order.OrderV2Entity;
import org.marketplace_lea.order.common.repository.order.OrderV2JpaRepository;
import org.marketplace_lea.order.domain.order.dto.OrderValidationDTO;
import org.marketplace_lea.common.common.event.ConsoEventPublisher;
import org.marketplace_lea.order.domain.order.events.OrderSubscriptionValidatedEvent;
import org.marketplace_lea.order.domain.order.events.OrderV2Event;
import org.marketplace_lea.order.domain.order.events.OrderValidationEvent;
import org.marketplace_lea.order.domain.order.form.OrderValidationForm;
import org.marketplace_lea.order.domain.order.handlers.OrderHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.marketplace_lea.order.common.entities.order.OrderStatus.CANCELED;
import static org.marketplace_lea.order.common.entities.order.OrderStatus.PENDING;
import static org.marketplace_lea.order.common.entities.order.OrderStatus.VALIDATED;

/**
 * Implementation of OrderHandler that orchestrates the complete order validation process.
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
public class DefaultOrderValidationHandler implements OrderHandler<OrderValidationForm, OrderValidationDTO> {
    private final OrderV2JpaRepository orderV2JpaRepository;
    private final CustomerV2JpaRepository customerV2JpaRepository;
    private final ConsoEventPublisher<OrderV2Event> eventPublisher;

    @Override
    @Transactional
    public OrderValidationDTO handle(OrderValidationForm command) {
        CustomerV2Entity customer = customerV2JpaRepository.findById(command.getCustomerId())
                .orElseThrow(() -> new ConsoEpargneNotFoundDataException("Client introuvable: " + command.getCustomerId()));


        var orderFound = orderV2JpaRepository.findById(command.getOrderId())
                .orElseThrow(() -> new ConsoEpargneNotFoundDataException("Client introuvable: " + command.getOrderId()));

        /// En fonction du status on execute la bonne méthode.
        switch (command.getStatus()) {
            case CANCEL -> handleCancelationOrder(orderFound);
            case VALIDATE -> handleValidationOrder(orderFound, command.getCustomerId());
        }

        return new OrderValidationDTO(orderFound.getId(), "Validation de la commande %s".formatted(orderFound.getId()));
    }


    public void handleCancelationOrder(OrderV2Entity orderV2Entity) {
        orderV2Entity.setStatus(CANCELED);
        orderV2Entity.setCanceledAt(LocalDateTime.now());
        var orderSaved = orderV2JpaRepository.save(orderV2Entity);
        log.info("Order canceled: {} at {}", orderSaved.getId(), orderSaved.getCanceledAt());
    }


    public void handleValidationOrder(OrderV2Entity order, String customerId) {
        boolean hasPaidOnline = order.isHasOnlinePayment();
        OrderV2Entity orderSaved;
        /// Cas si on doit valider manuellement la validation de la commande.
        if (PENDING.equals(order.getStatus())) {
            order.setStatus(VALIDATED);
            if (hasPaidOnline) {
                order.setPaidAt(LocalDateTime.now());
            } else {
                order.setDelivered(true);
                order.setDeliveredAt(LocalDateTime.now());
            }
            orderSaved = orderV2JpaRepository.save(order);
            log.info("Order pending validated: {} at {}", orderSaved.getId(), orderSaved.getValidatedAt());
        } else if (VALIDATED.equals(order.getStatus()) && order.isHasOnlinePayment()) {
            order.setDelivered(true);

            orderSaved = orderV2JpaRepository.save(order);
            log.info("Order validated and paid validated: {} at {}", orderSaved.getId(), orderSaved.getValidatedAt());
        } else {
            orderSaved = order;
        }

        /// Une fois que la commande est validée, on essaie de voir si la commande contient un kit, et créer une souscription.
        var orderItem = orderSaved.getAdherantKit();

        /// On envoie un pour la création d'une souscription si on a souscrit à un pack.
        orderItem.ifPresent(orderItemV2Entity -> {
            eventPublisher.publish(new OrderSubscriptionValidatedEvent(orderItemV2Entity, customerId));
            log.info("Send Order validated event for product type {}, brand {}", orderItemV2Entity.getProductType(), orderItemV2Entity.getBrand());
        });


        /// Si la commande est valide, on exécute un évenement pour
        if(orderSaved.isValidated()) {
            eventPublisher.publish(new OrderValidationEvent(null));
        }
    }
}
