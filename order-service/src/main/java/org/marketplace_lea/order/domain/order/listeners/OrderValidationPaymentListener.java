package org.marketplace_lea.order.domain.order.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.marketplace_lea.order.common.entities.order.OrderItemV2Entity;
import org.marketplace_lea.order.common.repository.order.OrderItemV2JpaRepository;
import org.marketplace_lea.order.domain.order.events.OrderValidationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderValidationPaymentListener {
    private final OrderItemV2JpaRepository orderItemRepository;
    // private final AccountTransactionManagerV2 accountTransactionManager;

    @EventListener
    @Async("eventTaskExecutor")
    public void handleDispatchingOrderOperation(OrderValidationEvent event) {
        try {
            log.info("[OrderValidationPaymentListener][handleDispatchingOrderOperation] Processing dispatch order operation for Id: {} at {}", 
                event.getId(), event.getTimestamp());
            
            var order = event.getOrder();
            
            if (order.isValidated()) {
                log.info("[OrderValidationPaymentListener][handleDispatchingOrderOperation] Start dispatching amount for Order Id: {} at {} with amount {}", 
                    event.getId(), LocalDateTime.now(), order.getTotalAmount());
                
                // Récupérer les items de type ADHERANT_KIT
                Optional<OrderItemV2Entity> adherantKitFound = order.getAdherantKit();
                
                log.info("[OrderValidationPaymentListener][handleDispatchingOrderOperation] Can create subscription: {}",
                        adherantKitFound.isPresent());
                
                if (adherantKitFound.isPresent()) {
                    // Traitement pour les kits d'adhésion
                    double commission = adherantKitFound
                        .map(adherantKit -> (adherantKit.getPrice().doubleValue() * 8) / 100)
                        .orElse(0.0);
                    
                    log.info("[OrderValidationPaymentListener][handleDispatchingOrderOperation] Get commission (Modification): {}", 
                        commission);
                    
                    var account = order.account();
                    // accountTransactionManager.dispatchSavingForAdhesionKit(account, Optional.of(adherantKit));
                    
                } else {
                    // Traitement pour les produits normaux
                    double commission = Optional.ofNullable(order.getTotalSavingAmount())
                        .orElse(0.0);
                    
                    log.info("[OrderValidationPaymentListener][handleDispatchingOrderOperation] Get commission (Original): {}", 
                        commission);
                    
                    // accountTransactionManager.dispatchSaving(order.account(), (float) commission);
                }
                
                log.info("[OrderValidationPaymentListener][handleDispatchingOrderOperation] End processing dispatch order operation for Id: {} at {}", 
                    event.getId(), LocalDateTime.now());
            }
            
        } catch (Exception ex) {
            log.error("[OrderValidationPaymentListener][handleDispatchingOrderOperation] Error while processing dispatch order operation for event Id: {} - message: {}", 
                event.getId(), ex.getMessage(), ex);
        }
    }
}